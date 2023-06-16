package com.booleanuk.recipes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "recipe_collections")
public class RecipeCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("recipe_collections")
    private User user;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "recipe_collection_recipes",
            joinColumns = @JoinColumn(name = "recipe_collection_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id"))
    private List<Recipe> recipes;

}
