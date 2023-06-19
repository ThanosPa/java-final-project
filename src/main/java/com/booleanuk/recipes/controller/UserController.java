package com.booleanuk.recipes.controller;

import com.booleanuk.recipes.model.Recipe;
import com.booleanuk.recipes.model.RecipeCollection;
import com.booleanuk.recipes.model.User;
import com.booleanuk.recipes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = this.userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login() {
        // Logic for user login
        return ResponseEntity.ok("Logged in successfully");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // Logic for user logout
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable int userId) {
        Optional<User> user = this.userRepository.findById(userId);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable int userId, @RequestBody User updatedUser) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUsername(updatedUser.getUsername());
            user.setPassword(updatedUser.getPassword());
            // Update other user details as needed
            User updated = userRepository.save(user);
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{userId}/recipes")
    public ResponseEntity<List<Recipe>> getUserRecipes(@PathVariable int userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Recipe> recipes = user.getRecipes();
            return ResponseEntity.ok(recipes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{userId}/recipes")
    public ResponseEntity<Void> saveUserRecipe(@PathVariable int userId, @RequestBody Recipe recipe) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.getRecipes().add(recipe);
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{userId}/recipes/{recipeId}")
    public ResponseEntity<Void> removeUserRecipe(@PathVariable int userId, @PathVariable int recipeId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Recipe> recipes = user.getRecipes();
            recipes.removeIf(recipe -> recipe.getId() == recipeId);
            userRepository.save(user);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{userId}/collections")
    public ResponseEntity<List<RecipeCollection>> getUserRecipeCollections(@PathVariable int userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<RecipeCollection> collections = user.getRecipeCollections();
            return ResponseEntity.ok(collections);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{userId}/collections")
    public ResponseEntity<RecipeCollection> createUserRecipeCollection(@PathVariable int userId, @RequestBody RecipeCollection collection) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.getRecipeCollections().add(collection);
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(collection);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{userId}/collections/{collectionId}")
    public ResponseEntity<RecipeCollection> updateUserRecipeCollection(@PathVariable int userId, @PathVariable int collectionId, @RequestBody RecipeCollection updatedCollection) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<RecipeCollection> collections = user.getRecipeCollections();
            Optional<RecipeCollection> collectionOptional = collections.stream()
                    .filter(collection -> collection.getId() ==collectionId)
                    .findFirst();
            if (collectionOptional.isPresent()) {
                RecipeCollection collection = collectionOptional.get();
                collection.setName(updatedCollection.getName());
                // Update other collection details as needed
                userRepository.save(user);
                return ResponseEntity.ok(collection);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{userId}/collections/{collectionId}/recipes")
    public ResponseEntity<Void> addRecipeToCollection(@PathVariable int userId, @PathVariable int collectionId, @RequestBody Recipe recipe) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<RecipeCollection> collections = user.getRecipeCollections();
            Optional<RecipeCollection> collectionOptional = collections.stream()
                    .filter(collection -> collection.getId() == collectionId)
                    .findFirst();
            if (collectionOptional.isPresent()) {
                RecipeCollection collection = collectionOptional.get();
                collection.getRecipes().add(recipe);
                userRepository.save(user);
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{userId}/collections/{collectionId}/recipes/{recipeId}")
    public ResponseEntity<Void> removeRecipeFromCollection(@PathVariable int userId, @PathVariable int collectionId, @PathVariable Long recipeId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<RecipeCollection> collections = user.getRecipeCollections();
            Optional<RecipeCollection> collectionOptional = collections.stream()
                    .filter(collection -> collection.getId() == collectionId)
                    .findFirst();
            if (collectionOptional.isPresent()) {
                RecipeCollection collection = collectionOptional.get();
                List<Recipe> recipes = collection.getRecipes();
                recipes.removeIf(recipe -> recipe.getId() == recipeId);
                userRepository.save(user);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}


