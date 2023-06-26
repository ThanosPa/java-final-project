package com.booleanuk.recipes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String Description;
    @Column(name = "cooking_time")
    private Duration CookingTime;
    @Column(name = "difficulty")
    private String Difficulty;
    @Column(name = "serving_size")
    private int ServingSize;
    @Column(name = "calories")
    private int Calories;
    @Column(name = "fat")
    private double Fat;
    @Column(name = "carbohydrates")
    private double Carbohydrates;
    @Column(name = "protein")
    private double Protein;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("recipes")
    private User user;
    @ManyToMany(mappedBy = "recipes")
    private List<RecipeCollection> recipeCollection;
    @OneToMany(mappedBy = "recipe")
    private List<Comment> comments;
    @OneToMany(mappedBy = "recipe")
    private List<Rating> ratings;
    @OneToMany(mappedBy = "recipe")
    private List<Ingredient> ingredients;
    @OneToMany(mappedBy = "recipe")
    private List<Instruction> instructions;
    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY)
    @JsonIgnore
    @JsonIgnoreProperties("recipe")

    private List<RecipeImage> images = new ArrayList<>();

    @Transient
    private List<String> imageUrls;

    // ... other fields, getters, and setters ...

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
    public Recipe() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Duration getCookingTime() {
        return CookingTime;
    }

    public void setCookingTime(Duration cookingTime) {
        CookingTime = cookingTime;
    }

    public String getDifficulty() {
        return Difficulty;
    }

    public void setDifficulty(String difficulty) {
        Difficulty = difficulty;
    }

    public int getServingSize() {
        return ServingSize;
    }

    public void setServingSize(int servingSize) {
        ServingSize = servingSize;
    }

    public int getCalories() {
        return Calories;
    }

    public void setCalories(int calories) {
        Calories = calories;
    }

    public double getFat() {
        return Fat;
    }

    public void setFat(double fat) {
        Fat = fat;
    }

    public double getCarbohydrates() {
        return Carbohydrates;
    }

    public void setCarbohydrates(double carbohydrates) {
        Carbohydrates = carbohydrates;
    }

    public double getProtein() {
        return Protein;
    }

    public void setProtein(double protein) {
        Protein = protein;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<RecipeCollection> getRecipeCollection() {
        return recipeCollection;
    }

    public void setRecipeCollection(List<RecipeCollection> recipeCollection) {
        this.recipeCollection = recipeCollection;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }

    public List<RecipeImage> getImages() {
        return images;
    }

    public void setImages(List<RecipeImage> images) {
        this.images = images;
    }
}
