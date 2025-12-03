package hh.flavorstory.web;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hh.flavorstory.domain.Category;
import hh.flavorstory.domain.CategoryRepository;
import hh.flavorstory.domain.Recipe;
import hh.flavorstory.domain.RecipeRepository;
import hh.flavorstory.domain.User;
import hh.flavorstory.domain.UserRepository;

@RestController
@RequestMapping("/api")
public class FlavorStoryRestController {

    private RecipeRepository recipeRepository;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;

    public FlavorStoryRestController(RecipeRepository recipeRepository, CategoryRepository categoryRepository,
            UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/recipes")
    public Iterable<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @GetMapping("/recipes/{id}")
    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    @PostMapping("/recipes")
    public Recipe createRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @GetMapping("/categories")
    public Iterable<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/categories/{id}")
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @PostMapping("/categories")
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @GetMapping("/users")
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @PostMapping("/users")
    public User createUser(User user) {
        return userRepository.save(user);
    }

}