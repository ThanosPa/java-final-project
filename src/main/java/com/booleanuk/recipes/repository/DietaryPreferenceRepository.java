package com.booleanuk.recipes.repository;

import com.booleanuk.recipes.model.DietaryPreference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DietaryPreferenceRepository extends JpaRepository<DietaryPreference,Integer> {
}
