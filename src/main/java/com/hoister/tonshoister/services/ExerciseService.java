package com.hoister.tonshoister.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoister.tonshoister.advisors.ExerciseNotFoundException;
import com.hoister.tonshoister.advisors.WorkoutNotFoundException;
import com.hoister.tonshoister.models.Exercise;
import com.hoister.tonshoister.models.Workout;
import com.hoister.tonshoister.repositories.ExerciseRepository;
import com.hoister.tonshoister.repositories.WorkoutRepository;

@Service
public class ExerciseService {

  @Autowired
  PrincipalService principalService;
  @Autowired
  private ExerciseRepository exerciseRepository;
  @Autowired
  private WorkoutRepository workoutRepository;

  public Exercise createExercise(Exercise exercise, Integer workoutId)
      throws WorkoutNotFoundException {
    exercise.setUserId(principalService.getAuthUserId());

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

  public Exercise updateExercise(Exercise exercise) throws ExerciseNotFoundException {
    Exercise foundExercise = exerciseRepository.findById(exercise.getId())
        .orElseThrow(() -> new ExerciseNotFoundException());

    foundExercise.setName(exercise.getName());
    foundExercise.setLoad(exercise.getLoad());
    foundExercise.setGoal(exercise.getGoal());
    foundExercise.setRestSeconds(exercise.getRestSeconds());
    foundExercise.setInstructions(exercise.getInstructions());

    return exerciseRepository.save(foundExercise);
  }

  public void deleteExercise(Integer id) throws ExerciseNotFoundException {
    if (!exerciseRepository.existsById(id)) {
      throw new ExerciseNotFoundException();
    }

    exerciseRepository.deleteById(id);
  }
}
