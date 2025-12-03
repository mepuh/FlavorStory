package hh.flavorstory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import hh.flavorstory.domain.Category;
import hh.flavorstory.domain.CategoryRepository;
import hh.flavorstory.domain.Recipe;
import hh.flavorstory.domain.RecipeRepository;
import hh.flavorstory.domain.UserRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FlavorStoryRestControllerIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setup() {
        recipeRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getAllRecipes_returnsSavedRecipes() {
        Category cat = new Category();
        cat.setName("Dessert");
        categoryRepository.save(cat);

        Recipe r = new Recipe();
        r.setTitle("Pie");
        r.setCategory(cat);
        r.setIngredients(List.of("apple"));
        r.setInstructions(List.of("bake"));
        recipeRepository.save(r);

        ResponseEntity<Recipe[]> resp = restTemplate.getForEntity("/api/recipes", Recipe[].class);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        Recipe[] body = resp.getBody();
        assertNotNull(body);
        assertTrue(Arrays.stream(body).anyMatch(rec -> "Pie".equals(rec.getTitle())));
    }

}
