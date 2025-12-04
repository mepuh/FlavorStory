package hh.flavorstory.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;
import hh.flavorstory.domain.RecipeRepository;
import hh.flavorstory.domain.Recipe;
import hh.flavorstory.domain.UserRepository;
import hh.flavorstory.domain.CategoryRepository;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class RecipeController {

    private RecipeRepository recipeRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;

    public RecipeController(RecipeRepository recipeRepository, UserRepository userRepository,
            CategoryRepository categoryRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    // List all recipes
    @GetMapping("/recipes")
    public String listRecipes(Model model) {
        model.addAttribute("recipes", recipeRepository.findAll());
        return "recipes";
    }

    // Add a new recipe (logged-in users only)
    @PostMapping("/recipes")
    public String addRecipe(Recipe recipe,
            @RequestParam(name = "ingredients", required = false) List<String> ingredients,
            @RequestParam(name = "instructions", required = false) List<String> instructions,
            Model model) {
        // Resolve category to managed entity if an id was provided
        if (recipe.getCategory() != null && recipe.getCategory().getCategoryId() != null) {
            Long catId = recipe.getCategory().getCategoryId();
            recipe.setCategory(categoryRepository.findById(catId).orElse(null));
        }

        // Set createdAt to today if not provided
        if (recipe.getCreatedAt() == null) {
            recipe.setCreatedAt(LocalDate.now());
        }

        // If creator not set, assign currently authenticated user (if available)
        if (recipe.getCreator() == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
                userRepository.findByUsername(auth.getName()).ifPresent(recipe::setCreator);
            }
        }

        // clean and set lists from multiple textarea inputs
        if (ingredients != null) {
            recipe.setIngredients(cleanList(ingredients));
        }
        if (instructions != null) {
            recipe.setInstructions(cleanList(instructions));
        }

        // Validate that at least one ingredient and one instruction exist
        List<String> ingr = recipe.getIngredients();
        List<String> instr = recipe.getInstructions();
        if (ingr == null || ingr.isEmpty() || instr == null || instr.isEmpty()) {
            model.addAttribute("errorMessage", "Please provide at least one ingredient and one instruction.");
            model.addAttribute("recipe", recipe);
            model.addAttribute("users", userRepository.findAll());
            model.addAttribute("categories", categoryRepository.findAll());
            return "newRecipe";
        }

        recipeRepository.save(recipe);
        return "redirect:/recipes";
    }

    // View specific recipe
    @GetMapping("/recipes/{id}")
    public String viewRecipe(@PathVariable("id") Long id, Model model) {
        model.addAttribute("recipe", recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid recipe Id:" + id)));
        return "recipe";
    }

    // Show form to add a new recipe (logged-in users only)
    @GetMapping("/recipes/new")
    public String newRecipeForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return "redirect:/login?loginRequired";
        }
        model.addAttribute("recipe", new Recipe());
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "newRecipe";
    }

    // Show form to edit an existing recipe (only creator or admin)
    @GetMapping("/recipes/{id}/edit")
    public String editRecipeForm(@PathVariable("id") Long id, Model model) {
        if (!isAdminOrRecipeOwner(id)) {
            return "redirect:/login";
        }
        model.addAttribute("recipe", recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid recipe Id:" + id)));
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "editRecipe";
    }

    // Update an existing recipe (only creator or admin)
    @PostMapping("/recipes/{id}/edit")
    public String editRecipe(@PathVariable("id") Long id, Recipe updatedRecipe,
            @RequestParam(name = "ingredients", required = false) List<String> ingredients,
            @RequestParam(name = "instructions", required = false) List<String> instructions,
            Model model) {
        if (!isAdminOrRecipeOwner(id)) {
            return "redirect:/login";
        }
        
        // Fetch the existing recipe to preserve creator and other metadata
        Recipe existingRecipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid recipe Id:" + id));
        
        updatedRecipe.setRecipeId(id);
        
        // Preserve the creator from the existing recipe
        updatedRecipe.setCreator(existingRecipe.getCreator());
        
        // Preserve the createdAt date
        updatedRecipe.setCreatedAt(existingRecipe.getCreatedAt());
        
        // Resolve category to managed entity if an id was provided
        if (updatedRecipe.getCategory() != null && updatedRecipe.getCategory().getCategoryId() != null) {
            Long catId = updatedRecipe.getCategory().getCategoryId();
            updatedRecipe.setCategory(categoryRepository.findById(catId).orElse(null));
        }
        
        // clean and set lists from multiple textarea inputs
        if (ingredients != null) {
            updatedRecipe.setIngredients(cleanList(ingredients));
        }
        if (instructions != null) {
            updatedRecipe.setInstructions(cleanList(instructions));
        }

        // Validate presence of at least one ingredient and instruction
        List<String> ingr = updatedRecipe.getIngredients();
        List<String> instr = updatedRecipe.getInstructions();
        if (ingr == null || ingr.isEmpty() || instr == null || instr.isEmpty()) {
            model.addAttribute("errorMessage", "Please provide at least one ingredient and one instruction.");
            model.addAttribute("recipe", updatedRecipe);
            model.addAttribute("users", userRepository.findAll());
            model.addAttribute("categories", categoryRepository.findAll());
            return "editRecipe";
        }

        recipeRepository.save(updatedRecipe);
        return "redirect:/recipes/{id}";
    }

    // Delete a recipe (only creator or admin)
    @PostMapping("/recipes/{id}/delete")
    public String deleteRecipe(@PathVariable("id") Long id) {
        if (!isAdminOrRecipeOwner(id)) {
            return "redirect:/login";
        }
        recipeRepository.deleteById(id);
        return "redirect:/recipes";
    }

    // Helper: returns true if current user is admin or owner of the recipe
    private boolean isAdminOrRecipeOwner(Long recipeId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        String currentUsername = auth.getName();
        return recipeRepository.findById(recipeId)
                .map(r -> r.getCreator() != null && r.getCreator().getUsername().equals(currentUsername)).orElse(false);
    }

    // Helper: cleans a list of strings by removing nulls, trimming, and filtering out empties
    private List<String> cleanList(List<String> items) {
        return items.stream()
                .filter(i -> i != null)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    // Toggle favorite for authenticated user
    @PostMapping("/recipes/{id}/favorite")
    public String toggleFavorite(@PathVariable("id") Long id, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return "redirect:/login";
        }

        // Check that both user and recipe exist
        var maybeUser = userRepository.findByUsername(auth.getName());
        var maybeRecipe = recipeRepository.findById(id);
        if (maybeUser.isEmpty() || maybeRecipe.isEmpty()) {
            return "redirect:/recipes";
        }
 
        // Both exist, proceed to toggle favorite
        var user = maybeUser.get();
        var recipe = maybeRecipe.get();
        if (user.getFavorites() == null) {
            // initialize list if null
            user.setFavorites(new java.util.ArrayList<>());
        }
 
        // Toggle favorite status
        if (user.getFavorites().stream().anyMatch(r -> r.getRecipeId().equals(recipe.getRecipeId()))) {
            user.removeFavorite(recipe);
        } else {
            user.addFavorite(recipe);
        }
        userRepository.save(user);

        // Redirect back to the referring page or recipe detail if no referer
        String referer = request.getHeader("referer");
        if (referer != null && !referer.isBlank()) {
            return "redirect:" + referer;
        }
        return "redirect:/recipes/" + id;
    }
}
