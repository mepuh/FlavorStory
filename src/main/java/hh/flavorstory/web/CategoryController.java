package hh.flavorstory.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import hh.flavorstory.domain.CategoryRepository;
import hh.flavorstory.domain.RecipeRepository;

@Controller
public class CategoryController {

    private CategoryRepository categoryRepository;
    private RecipeRepository recipeRepository;

    public CategoryController(CategoryRepository categoryRepository, RecipeRepository recipeRepository) {
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
    }

    // List all categories
    @GetMapping("/categories")
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "categories";
    }

    // View recipes in specific category
    @GetMapping("/categories/{id}")
    public String viewRecipesInCategory(@PathVariable("id") Long id, Model model) {
        model.addAttribute("category", categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id)));
        model.addAttribute("recipes", recipeRepository.findByCategory(categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id))));
        return "category";
    }

}