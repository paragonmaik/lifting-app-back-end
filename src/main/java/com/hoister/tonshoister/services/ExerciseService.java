package com.hoister.tonshoister.services;

import java.util.List;
import org.springframework.stereotype.Service;

import com.hoister.tonshoister.advisors.ExerciseNotFoundException;
import com.hoister.tonshoister.advisors.WorkoutNotFoundException;
import com.hoister.tonshoister.models.Exercise;
import com.hoister.tonshoister.models.Workout;
import com.hoister.tonshoister.repositories.ExerciseRepository;
import com.hoister.tonshoister.repositories.WorkoutRepository;

@Service
public class ExerciseService {
  private final ExerciseRepository exerciseRepository;
  private final WorkoutRepository workoutRepository;

  public ExerciseService(ExerciseRepository exerciseRepository,
      WorkoutRepository workoutRepository) {
    this.exerciseRepository = exerciseRepository;
    this.workoutRepository = workoutRepository;
  }

  public Exercise createExercise(Exercise exercise, Integer workoutId)
      throws WorkoutNotFoundException {

    Workout workout = workoutRepository.findById(workoutId)
        .orElseThrow(() -> new WorkoutNotFoundException());
    Exercise createdExercise = exerciseRepository.save(exercise);

    createdExercise.getWorkouts().add(workout);
    exerciseRepository.save(createdExercise);
    workout.getExercises().add(createdExercise);
    workoutRepository.save(workout);

    return createdExercise;
  }

  public List<Exercise> findAll() {
    List<Exercise> exercises = exerciseRepository.findAll();

    if (exercises.isEmpty()) {
      throw new ExerciseNotFoundException();
    }

    return exercises;
  }
}
