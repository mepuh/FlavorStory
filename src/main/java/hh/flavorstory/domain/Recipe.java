package hh.flavorstory.domain;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long recipeId;
    private String title;
    @Lob
    private String description;
    private String preparationTime;
    @Lob
    private List<String> ingredients;
    @Lob
    private List<String> instructions;
    @Lob
    private String story;
    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonIgnoreProperties("createdRecipes")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    @JsonIgnoreProperties("recipes")
    private Category category;

    @ManyToMany(mappedBy = "favorites")
    @JsonIgnoreProperties("favorites")
    private List<User> favoritedBy;

    public Recipe() {
    }

    public Recipe(String title, String description, String preparationTime, List<String> ingredients, List<String> instructions, String story,
            LocalDate createdAt, User creator, Category category, List<User> favoritedBy) {
        this.title = title;
        this.description = description;
        this.preparationTime = preparationTime;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.story = story;
        this.createdAt = createdAt;
        this.creator = creator;
        this.category = category;
        this.favoritedBy = favoritedBy;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(String preparationTime) {
        this.preparationTime = preparationTime;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<User> getFavoritedBy() {
        return favoritedBy;
    }

    public void setFavoritedBy(List<User> favoritedBy) {
        this.favoritedBy = favoritedBy;
    }

    @Override
    public String toString() {
        return "Recipe [recipeId=" + recipeId + ", title=" + title + ", preparationTime=" + preparationTime + ", description=" + description + ", ingredients="
                + ingredients + ", instructions=" + instructions + ", story=" + story + ", createdAt=" + createdAt
                + ", creator=" + creator + ", category=" + category + ", favoritedBy=" + favoritedBy + "]";
    }
    
}
