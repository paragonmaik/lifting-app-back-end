package com.hoister.tonshoister.repositories;

import org.springframework.data.repository.ListCrudRepository;

import com.hoister.tonshoister.models.Workout;

public interface WorkoutRepository extends ListCrudRepository<Workout, Integer> {
}
