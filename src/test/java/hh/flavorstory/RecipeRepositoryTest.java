package hh.flavorstory;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;

import hh.flavorstory.domain.Category;
import hh.flavorstory.domain.CategoryRepository;
import hh.flavorstory.domain.Recipe;
import hh.flavorstory.domain.RecipeRepository;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class RecipeRepositoryTest {

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void findByCategory_returnsRecipes() {
        Category cat = new Category();
        cat.setName("Dessert");
        categoryRepository.save(cat);

        Recipe r = new Recipe();
        r.setTitle("Cake");
        r.setCategory(cat);
        r.setIngredients(List.of("flour"));
        r.setInstructions(List.of("mix"));
        recipeRepository.save(r);

        Iterable<Recipe> found = recipeRepository.findByCategory(cat);
        boolean any = StreamSupport.stream(found.spliterator(), false)
                .anyMatch(rr -> "Cake".equals(rr.getTitle()));
        assertTrue(any, "Saved recipe should be found by category");
    }

}
