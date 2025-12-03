package hh.flavorstory;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import hh.flavorstory.domain.Category;
import hh.flavorstory.domain.CategoryRepository;
import hh.flavorstory.domain.Recipe;
import hh.flavorstory.domain.RecipeRepository;
import hh.flavorstory.domain.User;
import hh.flavorstory.domain.UserRepository;

@SpringBootApplication
public class FlavorstoryApplication {

	private static final Logger logger = LoggerFactory.getLogger(FlavorstoryApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(FlavorstoryApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CommandLineRunner demoData(RecipeRepository recipeRepository, CategoryRepository categoryRepository,
			UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		return (args) -> {

			logger.info("Creating default admin and user accounts");
			// admin
			User admin = new User();
			admin.setUsername("admin");
			admin.setPassword(passwordEncoder.encode("admin123"));
			admin.setEmail("admin@test.com");
			admin.setRole("ROLE_ADMIN");
			userRepository.save(admin);

			// regular user
			User testuser = new User();
			testuser.setUsername("testuser");
			testuser.setPassword(passwordEncoder.encode("testuser123"));
			testuser.setEmail("user@test.com");
			testuser.setRole("ROLE_USER");
			userRepository.save(testuser);

			// another regular user
			User user2 = new User("foodlover", passwordEncoder.encode("foodlover123"), "foodlover@test.com", "ROLE_USER",null, null);
			userRepository.save(user2);

			logger.info("Saving a few categories");
			Category appetizers = new Category("Appetizers", null);
			categoryRepository.save(appetizers);
			Category mains = new Category("Main Courses", null);
			categoryRepository.save(mains);
			Category baking = new Category("Baking & Desserts", null);
			categoryRepository.save(baking);
			Category beverages = new Category("Beverages", null);
			categoryRepository.save(beverages);
			Category soups = new Category("Soups", null);
			categoryRepository.save(soups);
			Category salads = new Category("Salads", null);
			categoryRepository.save(salads);
			Category mexican = new Category("Mexican", null);
			categoryRepository.save(mexican);
			Category other = new Category("Other", null);
			categoryRepository.save(other);

			logger.info("Saving a few recipes");
			// 1. Lasagna recipe
			Recipe recipe1 = new Recipe("Mom's Best Beef Lasagna",
					"Juicy, cheesy lasagne — the ultimate Sunday dinner for the whole family.", "75 minutes", List.of(
							"12-15 lasagne sheets (no-boil or fresh)",
							"500 g ground beef",
							"1 onion, finely chopped",
							"2 garlic cloves",
							"1 can (400 g) crushed tomatoes",
							"2 tbsp tomato paste",
							"1 tsp dried oregano & basil",
							"salt & black pepper",
							"50 g butter",
							"5 tbsp flour",
							"800 ml milk",
							"200 g grated cheese (mozzarella + emmental)"),
					List.of(
							"Brown the beef, add onion and garlic.",
							"Add crushed tomatoes, paste and spices. Simmer 15 min.",
							"Melt butter, stir in flour, gradually add milk → thick béchamel.",
							"Stir in cheese until melted.",
							"Layer in a greased dish: cheese sauce → lasagne sheets → meat sauce.",
							"Finish with a thick layer of cheese sauce and extra grated cheese.",
							"Bake at 200 °C for 40-45 minutes."),
					"This was our family's Sunday dinner throughout my entire childhood. Mom always made double because the neighbors' kids would show up the moment the smell drifted into the hallway. Whenever I make it now, everything feels right with the world.",
					LocalDate.of(2024, 8, 10), testuser, mains, null);
			recipeRepository.save(recipe1);

			// 2. Chicken Noodle Soup recipe
			Recipe recipe2 = new Recipe("Classic Chicken Noodle Soup",
					"A comforting bowl of homemade chicken noodle soup.", "60 minutes", List.of(
							"1 whole chicken (about 1.5 kg)",
							"3 carrots, sliced",
							"3 celery stalks, sliced",
							"1 onion, chopped",
							"3 garlic cloves, minced",
							"200 g egg noodles",
							"2 liters chicken broth",
							"salt & pepper to taste",
							"fresh parsley for garnish"),
					List.of(
							"In a large pot, place the whole chicken and cover with chicken broth. Bring to a boil.",
							"Reduce heat and simmer for about 45 minutes until the chicken is cooked through.",
							"Remove the chicken, let it cool, then shred the meat, discarding bones and skin.",
							"Add carrots, celery, onion, and garlic to the pot. Simmer for 15 minutes until vegetables are tender.",
							"Add shredded chicken back into the pot.",
							"Stir in egg noodles and cook for an additional 8-10 minutes until noodles are al dente.",
							"Season with salt and pepper to taste. Garnish with fresh parsley before serving."),
					"This classic chicken noodle soup has been a family favorite for generations. It's perfect for chilly days or when someone needs a little extra comfort.",
					LocalDate.of(2025, 9, 14), user2, soups, null);
			recipeRepository.save(recipe2);

			// 3. Chocolate Chip Cookies recipe
			Recipe recipe3 = new Recipe("Chocolate Chip Cookies",
					"Crispy on the edges, chewy in the center chocolate chip cookies.", "30 minutes", List.of(
							"225 g all-purpose flour",
							"1/2 tsp baking soda",
							"1/2 tsp salt",
							"170 g unsalted butter, melted",
							"150 g brown sugar",
							"100 g granulated sugar",
							"1 tbsp vanilla extract",
							"1 large egg + 1 egg yolk",
							"200 g chocolate chips"),
					List.of(
							"Preheat oven to 175 °C. Line baking sheets with parchment paper.",
							"In a bowl, whisk together flour, baking soda, and salt.",
							"In another bowl, mix melted butter, brown sugar, and granulated sugar until smooth.",
							"Beat in vanilla extract, egg, and egg yolk until light and creamy.",
							"Gradually blend in the dry ingredients until just combined.",
							"Fold in chocolate chips.",
							"Drop spoonfuls of dough onto prepared baking sheets.",
							"Bake for 10-12 minutes until edges are golden but centers are still soft.",
							"Let cool on baking sheets for a few minutes before transferring to wire racks."),
					"These chocolate chip cookies are my go-to recipe whenever I need a quick treat. They always turn out perfect and are loved by everyone who tries them.",
					LocalDate.of(2025, 10, 28), testuser, baking, null);
			recipeRepository.save(recipe3);

		};
	}
}