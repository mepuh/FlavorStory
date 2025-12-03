package hh.flavorstory.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import hh.flavorstory.domain.RecipeRepository;
import hh.flavorstory.domain.UserRepository;
import hh.flavorstory.domain.Category;
import hh.flavorstory.domain.CategoryRepository;

@Controller
public class AdminController {

    private RecipeRepository recipeRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;

    public AdminController(RecipeRepository recipeRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    // Show admin dashboard (admin only)
    @GetMapping("/admin")
    public String adminDashboard() {
        return "adminDashboard";
    }

    // List all recipes (admin only)
    @GetMapping("/admin/recipes")
    public String adminListRecipes(Model model) {
        model.addAttribute("recipes", recipeRepository.findAll());
        return "adminRecipes";
    }

    // Delete a recipe (admin only)
    @PostMapping("/admin/recipes/{id}/delete")
    public String adminDeleteRecipe(@PathVariable("id") Long id) {
        recipeRepository.deleteById(id);
        return "redirect:/admin/recipes";
    }

    // List all categories (admin only)
    @GetMapping("/admin/categories")
    public String adminListCategories(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "adminCategories";
    }

    // Show form to add a new category (admin only)
    @GetMapping("/admin/categories/new")
    public String adminNewCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "newCategory";
    }

    // Add a new category (admin only)
    @PostMapping("/admin/categories/new")
    public String adminAddCategory(Category category) {
        categoryRepository.save(category);
        return "redirect:/admin/categories";
    }

    // Show form to edit a category (admin only)
    @GetMapping("/admin/categories/{id}/edit")
    public String adminEditCategoryForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("category", categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id)));
        model.addAttribute("categories", categoryRepository.findAll());
        return "editCategory";
    }

    // Update a category (admin only)
    @PostMapping("/admin/categories/{id}/edit")
    public String adminEditCategory(@PathVariable("id") Long id, Category updatedCategory) {
        updatedCategory.setCategoryId(id);
        categoryRepository.save(updatedCategory);
        return "redirect:/admin/categories";
    }

    // Delete a category (admin only)
    @PostMapping("/admin/categories/{id}/delete")
    public String adminDeleteCategory(@PathVariable("id") Long id) {
        categoryRepository.deleteById(id);
        return "redirect:/admin/categories";
    }

    // List all users (admin only)
    @GetMapping("/admin/users")
    public String adminListUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users";
    }

    // Delete a user (admin only)
    @PostMapping("/admin/users/{id}/delete")
    public String adminDeleteUser(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }

}
