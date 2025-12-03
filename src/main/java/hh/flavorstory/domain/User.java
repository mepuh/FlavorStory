package hh.flavorstory.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    private String username;
    private String password;
    private String email;
    private String role;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator")
    @JsonIgnoreProperties("creator")
    private List<Recipe> createdRecipes;

    @ManyToMany
    @JoinTable(name = "user_favorites", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "recipeId"))
    @JsonIgnoreProperties("favoritedBy")
    private List<Recipe> favorites;

    public User() {
    }

    public User(String username, String password, String email, String role,
            List<Recipe> createdRecipes, List<Recipe> favorites) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.createdRecipes = createdRecipes;
        this.favorites = favorites;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Recipe> getCreatedRecipes() {
        return createdRecipes;
    }

    public void setCreatedRecipes(List<Recipe> createdRecipes) {
        this.createdRecipes = createdRecipes;
    }

    public List<Recipe> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Recipe> favorites) {
        this.favorites = favorites;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", username=" + username + ", password=" + password + ", email=" + email
                + ", role=" + role + ", createdRecipes=" + createdRecipes + ", favorites=" + favorites + "]";
    }

    // Methods to add or remove a favorite recipe
    public void addFavorite(Recipe recipe) {
        this.favorites.add(recipe);
    }

    public void removeFavorite(Recipe recipe) {
        this.favorites.remove(recipe);
    }

}
