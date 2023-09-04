package com.hoister.tonshoister.exercise;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hoister.tonshoister.advisors.ExerciseNotFoundException;
import com.hoister.tonshoister.advisors.UserIdDoesNotMatchException;
import com.hoister.tonshoister.advisors.WorkoutNotFoundException;
import com.hoister.tonshoister.models.Exercise;
import com.hoister.tonshoister.models.GoalType;
import com.hoister.tonshoister.models.Workout;
import com.hoister.tonshoister.repositories.ExerciseRepository;
import com.hoister.tonshoister.repositories.WorkoutRepository;
import com.hoister.tonshoister.services.ExerciseService;
import com.hoister.tonshoister.services.PrincipalService;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceTests {

  @Mock
  PrincipalService principalService;
  @Mock
  ExerciseRepository exerciseRepository;
  @Mock
  WorkoutRepository workoutRepository;
  @InjectMocks
  ExerciseService exerciseService;

  @Test
  public void createExerciseSuccess() throws WorkoutNotFoundException {
    String userId = "uuid";
    Exercise exercise = new Exercise(
        "High Bar Squat", 120, GoalType.STRENGTH, 150, "Bar rests at the traps.");
    Workout workout = new Workout("Workout A", 10, "A really tough workout.");
    exercise.setUserId(userId);

    when(principalService.getAuthUserId()).thenReturn(userId);
    when(workoutRepository.findById(1)).thenReturn(Optional.of(workout));
    when(exerciseRepository.save(exercise)).thenReturn(exercise);

    Exercise createdExercise = exerciseService.createExercise(exercise, 1);

    assertEquals(exercise, createdExercise);

    verify(exerciseRepository, times(2)).save(createdExercise);
  }

  @Test
  public void createExerciseThrowsException() throws WorkoutNotFoundException {
    Exercise exercise = new Exercise(
        "High Bar Squat", 120, GoalType.STRENGTH, 150, "Bar rests at the traps.");

    when(workoutRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(WorkoutNotFoundException.class, () -> {
      exerciseService.createExercise(exercise, 1);
    });

    verify(workoutRepository).findById(1);
  }

  @Test
  public void findAllExercisesSuccess() throws ExerciseNotFoundException {
    Exercise exercise = new Exercise(
        "High Bar Squat", 120, GoalType.STRENGTH, 150, "Bar rests at the traps.");
    List<Exercise> exercises = new ArrayList<Exercise>();
    exercises.add(exercise);
    String userId = "uuid";

    when(principalService.getAuthUserId()).thenReturn(userId);
    when(exerciseRepository.findAllByUserId(userId)).thenReturn(exercises);

    List<Exercise> requestedExercises = exerciseService.findAllByUserId();

    assertEquals(exercises, requestedExercises);
    verify(exerciseRepository).findAllByUserId(userId);
  }

  @Test
  public void findAllExercisesThrowsException() throws ExerciseNotFoundException {
    List<Exercise> exercises = new ArrayList<Exercise>();
    String userId = "uuid";

    when(principalService.getAuthUserId()).thenReturn(userId);
    when(exerciseRepository.findAllByUserId(userId)).thenReturn(exercises);

    assertThrows(ExerciseNotFoundException.class, () -> {
      exerciseService.findAllByUserId();
    });

    verify(exerciseRepository).findAllByUserId(userId);
  }

  @Test
  public void updateExerciseSuccess() throws ExerciseNotFoundException {
    String userId = "uuid";
    Exercise exercise1 = new Exercise(
        1, "High Bar Squat", 120, GoalType.STRENGTH, 150, "Bar rests at the traps.");
    Exercise exercise2 = new Exercise(
        1, "Front Squat", 100, GoalType.HYPERTROPHY, 180, "No instructions.");
    exercise1.setUserId(userId);
    exercise2.setUserId(userId);

    when(principalService.getAuthUserId()).thenReturn(userId);
    when(exerciseRepository.findById(1)).thenReturn(Optional.of(exercise1));
    when(exerciseRepository.save(exercise1)).thenReturn(exercise2);

    Exercise updatedExercise = exerciseService.updateExercise(exercise1);

    assertNotEquals(exercise1.getName(), updatedExercise.getName());
    assertNotEquals(exercise1.getLoad(), updatedExercise.getLoad());
    assertNotEquals(exercise1.getGoal(), updatedExercise.getGoal());
    assertNotEquals(exercise1.getRestSeconds(), updatedExercise.getRestSeconds());
    assertNotEquals(exercise1.getInstructions(), updatedExercise.getInstructions());

    verify(exerciseRepository).save(exercise1);
    verify(exerciseRepository).findById(1);
  }

  @Test
  public void updateExerciseThrowsException() throws ExerciseNotFoundException {
    Exercise exercise = new Exercise(
        1, "High Bar Squat", 120, GoalType.STRENGTH, 150, "Bar rests at the traps.");

    when(exerciseRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(ExerciseNotFoundException.class, () -> {
      exerciseService.updateExercise(exercise);
    });

    verify(exerciseRepository).findById(1);
  }

  @Test
  public void updateExerciseThrowsUserIdDoesNotMatchException() {
    String userId = "uuid";
    Exercise exercise = new Exercise(
        1, "High Bar Squat", 120, GoalType.STRENGTH, 150, "Bar rests at the traps.");
    exercise.setUserId(userId);

    when(exerciseRepository.findById(1)).thenReturn(Optional.of(exercise));

    assertThrows(UserIdDoesNotMatchException.class, () -> {
      exerciseService.updateExercise(exercise);
    });

    verify(exerciseRepository).findById(1);
  }

  @Test
  public void deleteExerciseSuccess() throws Exception {
    when(exerciseRepository.existsById(1)).thenReturn(true);
    exerciseService.deleteExercise(1);
    
    verify(exerciseRepository).existsById(1);
    verify(exerciseRepository).deleteById(1);
  }

  @Test
  public void deleteExerciseThrowsException() throws Exception {
    when(exerciseRepository.existsById(1)).thenReturn(false);

    assertThrows(ExerciseNotFoundException.class, () -> {
      exerciseService.deleteExercise(1);
    });

    verify(exerciseRepository).existsById(1);
  }

}
