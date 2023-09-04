package com.hoister.tonshoister.repositories;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

import com.hoister.tonshoister.models.Exercise;

public interface ExerciseRepository extends ListCrudRepository<Exercise, Integer> {
  List<Exercise> findAllByUserId(String userId);
}
