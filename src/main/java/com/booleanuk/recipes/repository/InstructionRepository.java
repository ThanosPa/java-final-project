package com.booleanuk.recipes.repository;

import com.booleanuk.recipes.model.Instruction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructionRepository extends JpaRepository<Instruction,Integer> {
}
