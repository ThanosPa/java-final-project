package com.booleanuk.recipes.repository;

import com.booleanuk.recipes.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
}
