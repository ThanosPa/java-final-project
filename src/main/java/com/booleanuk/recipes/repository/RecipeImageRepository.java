package com.booleanuk.recipes.repository;

import com.booleanuk.recipes.model.RecipeImage;
import com.booleanuk.recipes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecipeImageRepository extends JpaRepository<RecipeImage,Integer> {
    Optional<RecipeImage> findByImageUrl(String email);
}
