package com.booleanuk.recipes.repository;

import com.booleanuk.recipes.model.RecipeCollection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeCollectionRepository extends JpaRepository<RecipeCollection,Integer> {
}
