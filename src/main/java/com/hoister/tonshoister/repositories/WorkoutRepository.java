package com.hoister.tonshoister.repositories;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

import com.hoister.tonshoister.models.Workout;

public interface WorkoutRepository extends ListCrudRepository<Workout, Integer> {
  List<Workout> findAllByUserId(String userId);
}
