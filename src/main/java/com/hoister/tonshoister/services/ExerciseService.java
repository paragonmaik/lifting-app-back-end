package com.hoister.tonshoister.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoister.tonshoister.advisors.ExerciseNotFoundException;
import com.hoister.tonshoister.advisors.UserIdDoesNotMatchException;
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

  public List<Exercise> findAllByUserId() {
    List<Exercise> exercises = exerciseRepository
        .findAllByUserId(principalService.getAuthUserId());

    if (exercises.isEmpty()) {
      throw new ExerciseNotFoundException();
    }

    return exercises;
  }

  public Exercise updateExercise(Exercise exercise) throws ExerciseNotFoundException {
    Exercise foundExercise = exerciseRepository.findById(exercise.getId())
        .orElseThrow(() -> new ExerciseNotFoundException());

    if (!foundExercise.getUserId().equals(principalService.getAuthUserId())) {
      throw new UserIdDoesNotMatchException();
    }

    foundExercise.setName(exercise.getName());
    foundExercise.setLoad(exercise.getLoad());
    foundExercise.setSets(exercise.getSets());
    foundExercise.setReps(exercise.getReps());
    foundExercise.setGoal(exercise.getGoal());
    foundExercise.setRestSeconds(exercise.getRestSeconds());
    foundExercise.setInstructions(exercise.getInstructions());

    return exerciseRepository.save(foundExercise);
  }

  public void deleteExercise(Integer id) throws ExerciseNotFoundException {
    Exercise foundExercise = exerciseRepository.findById(id)
        .orElseThrow(() -> new ExerciseNotFoundException());

    if (!foundExercise.getUserId().equals(principalService.getAuthUserId())) {
      throw new UserIdDoesNotMatchException();
    }

    exerciseRepository.deleteById(id);
  }
}
