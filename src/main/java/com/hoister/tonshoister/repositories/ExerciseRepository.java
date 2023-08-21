package com.hoister.tonshoister.repositories;

import org.springframework.data.repository.ListCrudRepository;

import com.hoister.tonshoister.models.Exercise;

public interface ExerciseRepository extends ListCrudRepository<Exercise, Integer> {
}
