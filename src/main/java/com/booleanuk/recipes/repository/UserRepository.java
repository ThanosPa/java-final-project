package com.booleanuk.recipes.repository;

import com.booleanuk.recipes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
}
