package com.booleanuk.recipes.controller;

import com.booleanuk.recipes.ImageUtil;
import com.booleanuk.recipes.model.*;
import com.booleanuk.recipes.repository.RecipeImageRepository;
import com.booleanuk.recipes.repository.RecipeRepository;
import com.booleanuk.recipes.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecipeImageRepository recipeImageRepository;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SessionFactory sessionFactory;
    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {

        List<Recipe> recipes = recipeRepository.findAll();
        for (Recipe recipe : recipes) {
            List<String> imageUrls = recipe.getImages().stream()
                    .map(RecipeImage::getImageUrl)
                    .collect(Collectors.toList());
            recipe.setImageUrls(imageUrls);
        }
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable int recipeId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isPresent()) {
            Recipe recipe = recipeOptional.get();
            return ResponseEntity.ok(recipe);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Recipe> createRecipe(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("name") String name,
           @RequestParam("description") String description,
           @RequestParam("cooking_time") int cookingTime,
           @RequestParam("difficulty") String difficulty,
           @RequestParam("serving_size") int servingSize,
           @RequestParam("calories") int calories,
           @RequestParam("fat") double fat,
           @RequestParam("carbohydrates") double carbohydrates,
           @RequestParam("protein") double protein,
           @RequestParam("image") MultipartFile image
    ) throws IOException {
        Recipe newRecipe = new Recipe();
        newRecipe.setName(name);
        newRecipe.setDescription(description);
        Duration cooking_time_duration= Duration.ofMinutes(cookingTime);
        newRecipe.setCookingTime(cooking_time_duration);
        newRecipe.setDifficulty(difficulty);
        newRecipe.setServingSize(servingSize);
        newRecipe.setCalories(calories);
        newRecipe.setFat(fat);
        newRecipe.setCarbohydrates(carbohydrates);
        newRecipe.setProtein(protein);
        Optional<User> user = userRepository.findByEmail(userDetails.getUsername());
        Recipe createdRecipe = null;
        if(user.isPresent()){
            newRecipe.setUser(user.get());

            createdRecipe = recipeRepository.save(newRecipe);

            if (!image.isEmpty()) {
                // Save or process the image
                System.out.println("test");

                RecipeImage newRecipeImage= RecipeImage.builder()
                        .image(image.getBytes()).recipe(createdRecipe).build();
                String imageUrl = "images/" + newRecipeImage.getId();
                String fileExtension = image.getOriginalFilename(); // Get the file extension from the original file name
                String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString() + fileExtension;
                newRecipeImage.setImageUrl(fileName);
                RecipeImage createdImage =recipeImageRepository.save(newRecipeImage);
                //newRecipe.getImages().add(createdImage);


            }

        }


        // Save the recipe using the service class
        //recipeRepository.save(newRecipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipe);
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable int recipeId, @RequestBody Recipe requestRecipe) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isPresent()) {
            Recipe recipe = recipeOptional.get();
            recipe.setName(requestRecipe.getName());
            recipe.setDescription(requestRecipe.getDescription());
            // Update other recipe details as needed
            Recipe updatedRecipe = recipeRepository.save(recipe);
            return ResponseEntity.ok(updatedRecipe);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable int recipeId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isPresent()) {
            Recipe recipe = recipeOptional.get();
            recipeRepository.delete(recipe);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{recipeId}/ingredients")
    public ResponseEntity<Void> addIngredientToRecipe(@PathVariable int recipeId, @RequestBody Ingredient ingredient) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isPresent()) {
            Recipe recipe = recipeOptional.get();
            recipe.getIngredients().add(ingredient);
            recipeRepository.save(recipe);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{recipeId}/ingredients/{ingredientId}")
    public ResponseEntity<Ingredient> updateIngredientInRecipe(@PathVariable int recipeId, @PathVariable int ingredientId, @RequestBody Ingredient updatedIngredient) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isPresent()) {
            Recipe recipe = recipeOptional.get();
            List<Ingredient> ingredients = recipe.getIngredients();
            Optional<Ingredient> ingredientOptional = ingredients.stream()
                    .filter(ingredient -> ingredient.getId() ==ingredientId)
                    .findFirst();
            if (ingredientOptional.isPresent()) {
                Ingredient ingredient = ingredientOptional.get();
                ingredient.setName(updatedIngredient.getName());
                ingredient.setQuantity(updatedIngredient.getQuantity());
                // Update other ingredient details as needed
                recipeRepository.save(recipe);
                return ResponseEntity.ok(ingredient);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{recipeId}/ingredients/{ingredientId}")
    public ResponseEntity<Void> removeIngredientFromRecipe(@PathVariable int recipeId, @PathVariable int ingredientId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isPresent()) {
            Recipe recipe = recipeOptional.get();
            List<Ingredient> ingredients = recipe.getIngredients();
            ingredients.removeIf(ingredient -> ingredient.getId() == ingredientId);
            recipeRepository.save(recipe);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

// Implement endpoints for instructions, comments, ratings in a similar manner
@PostMapping("/{recipeId}/instructions")
public ResponseEntity<Void> addInstructionToRecipe(@PathVariable int recipeId, @RequestBody Instruction instruction) {
    Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
    if (recipeOptional.isPresent()) {
        Recipe recipe = recipeOptional.get();
        recipe.getInstructions().add(instruction);
        recipeRepository.save(recipe);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    } else {
        return ResponseEntity.notFound().build();
    }
}

    @PutMapping("/{recipeId}/instructions/{instructionId}")
    public ResponseEntity<Instruction> updateInstructionInRecipe(@PathVariable int recipeId, @PathVariable int instructionId, @RequestBody Instruction updatedInstruction) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isPresent()) {
            Recipe recipe = recipeOptional.get();
            List<Instruction> instructions = recipe.getInstructions();
            Optional<Instruction> instructionOptional = instructions.stream()
                    .filter(instruction -> instruction.getId() == instructionId)
                    .findFirst();
            if (instructionOptional.isPresent()) {
                Instruction instruction = instructionOptional.get();
                instruction.setInstruction(updatedInstruction.getInstruction());
                // Update other instruction details as needed
                recipeRepository.save(recipe);
                return ResponseEntity.ok(instruction);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{recipeId}/instructions/{instructionId}")
    public ResponseEntity<Void> removeInstructionFromRecipe(@PathVariable int recipeId, @PathVariable int instructionId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isPresent()) {
            Recipe recipe = recipeOptional.get();
            List<Instruction> instructions = recipe.getInstructions();
            instructions.removeIf(instruction -> instruction.getId() == instructionId);
            recipeRepository.save(recipe);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{recipeId}/comments")
    public ResponseEntity<Void> addCommentToRecipe(@PathVariable int recipeId, @RequestBody Comment comment) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isPresent()) {
            Recipe recipe = recipeOptional.get();
            recipe.getComments().add(comment);
            recipeRepository.save(recipe);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{recipeId}/comments/{commentId}")
    public ResponseEntity<Comment> updateCommentInRecipe(@PathVariable int recipeId, @PathVariable int commentId, @RequestBody Comment updatedComment) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isPresent()) {
            Recipe recipe = recipeOptional.get();
            List<Comment> comments = recipe.getComments();
            Optional<Comment> commentOptional = comments.stream()
                    .filter(comment -> comment.getId() == commentId)
                    .findFirst();
            if (commentOptional.isPresent()) {
                Comment comment = commentOptional.get();
                comment.setText(updatedComment.getText());
                // Update other comment details as needed
                recipeRepository.save(recipe);
                return ResponseEntity.ok(comment);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{recipeId}/comments/{commentId}")
    public ResponseEntity<Void> removeCommentFromRecipe(@PathVariable int recipeId, @PathVariable int commentId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isPresent()) {
            Recipe recipe = recipeOptional.get();
            List<Comment> comments = recipe.getComments();
            comments.removeIf(comment -> comment.getId() ==commentId );
            recipeRepository.save(recipe);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{recipeId}/ratings")
    public ResponseEntity<Void> addRatingToRecipe(@PathVariable int recipeId, @RequestBody Rating rating) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isPresent()) {
            Recipe recipe = recipeOptional.get();
            recipe.getRatings().add(rating);
            recipeRepository.save(recipe);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{recipeId}/ratings/{ratingId}")
    public ResponseEntity<Rating> updateRatingInRecipe(@PathVariable int recipeId, @PathVariable int ratingId, @RequestBody Rating updatedRating) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isPresent()) {
            Recipe recipe = recipeOptional.get();
            List<Rating> ratings = recipe.getRatings();
            Optional<Rating> ratingOptional = ratings.stream()
                    .filter(rating -> rating.getId() ==ratingId)
                    .findFirst();
            if (ratingOptional.isPresent()) {
                Rating rating = ratingOptional.get();
                rating.setValue(updatedRating.getValue());
                // Update other rating details as needed
                recipeRepository.save(recipe);
                return ResponseEntity.ok(rating);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{recipeId}/ratings/{ratingId}")
    public ResponseEntity<Void> removeRatingFromRecipe(@PathVariable int recipeId, @PathVariable int ratingId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isPresent()) {
            Recipe recipe = recipeOptional.get();
            List<Rating> ratings = recipe.getRatings();
            ratings.removeIf(rating -> rating.getId() == ratingId );
            recipeRepository.save(recipe);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
