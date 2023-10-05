package com.hoister.tonshoister.exercise;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hoister.tonshoister.advisors.*;
import com.hoister.tonshoister.models.*;
import com.hoister.tonshoister.repositories.*;
import com.hoister.tonshoister.services.*;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceTests {
  private String userId = "37755df9-5607-495e-b5d4-da4f01f7c665";
  Exercise exercise1;
  Exercise exercise2;
  Workout workout;

  @Mock
  PrincipalService principalService;
  @Mock
  ExerciseRepository exerciseRepository;
  @Mock
  WorkoutRepository workoutRepository;
  @InjectMocks
  ExerciseService exerciseService;

  @BeforeEach
  public void setEntities() {
    exercise1 = new Exercise(
        1, userId, "High Bar Squat", 120, GoalType.STRENGTH,
        150, "Bar rests at the traps.", null, 3, 5, 1, new HashSet<Workout>());

    exercise2 = new Exercise(
        1, userId, "Front Squat", 100, GoalType.HYPERTROPHY,
        180, "No instructions.", null, 3, 5, 1, null);

    workout = new Workout(
        null, null, "Workout A", 10, "A really tough workout.",
        null, null, new HashSet<Exercise>());
  }

  @Test
  public void createExerciseSuccess() throws WorkoutNotFoundException {
    when(principalService.getAuthUserId()).thenReturn(userId);
    when(workoutRepository.findById(1)).thenReturn(Optional.of(workout));
    when(exerciseRepository.save(exercise1)).thenReturn(exercise1);

    Exercise createdExercise = exerciseService.createExercise(exercise1, 1);

    assertEquals(createdExercise, exercise1);

    verify(exerciseRepository, times(2)).save(createdExercise);
  }

  @Test
  public void createExerciseThrowsException() throws WorkoutNotFoundException {
    when(workoutRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(WorkoutNotFoundException.class, () -> {
      exerciseService.createExercise(exercise1, 1);
    });

    verify(workoutRepository).findById(1);
  }

  @Test
  public void findAllExercisesSuccess() throws ExerciseNotFoundException {
    List<Exercise> exercises = new ArrayList<Exercise>();
    exercises.add(exercise1);

    when(principalService.getAuthUserId()).thenReturn(userId);
    when(exerciseRepository.findAllByUserId(userId)).thenReturn(exercises);

    List<Exercise> requestedExercises = exerciseService.findAllByUserId();

    assertEquals(requestedExercises, exercises);

    verify(exerciseRepository).findAllByUserId(userId);
  }

  @Test
  public void findAllExercisesThrowsException() throws ExerciseNotFoundException {
    List<Exercise> exercises = new ArrayList<Exercise>();

    when(principalService.getAuthUserId()).thenReturn(userId);
    when(exerciseRepository.findAllByUserId(userId)).thenReturn(exercises);

    assertThrows(ExerciseNotFoundException.class, () -> {
      exerciseService.findAllByUserId();
    });

    verify(exerciseRepository).findAllByUserId(userId);
  }

  @Test
  public void updateExerciseSuccess() throws ExerciseNotFoundException {
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
    when(exerciseRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(ExerciseNotFoundException.class, () -> {
      exerciseService.updateExercise(exercise1);
    });

    verify(exerciseRepository).findById(1);
  }

  @Test
  public void updateExerciseThrowsUserIdDoesNotMatchException() {
    when(exerciseRepository.findById(1)).thenReturn(Optional.of(exercise1));

    assertThrows(UserIdDoesNotMatchException.class, () -> {
      exerciseService.updateExercise(exercise1);
    });

    verify(exerciseRepository).findById(1);
  }

  @Test
  public void deleteExerciseSuccess() throws Exception {
    when(principalService.getAuthUserId()).thenReturn(userId);
    when(exerciseRepository.findById(1)).thenReturn(Optional.of(exercise1));
    exerciseService.deleteExercise(1);

    verify(exerciseRepository).findById(1);
    verify(exerciseRepository).deleteById(1);
  }

  @Test
  public void deleteExerciseThrowsException() throws Exception {
    when(exerciseRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(ExerciseNotFoundException.class, () -> {
      exerciseService.deleteExercise(1);
    });

    verify(exerciseRepository).findById(1);
  }

  @Test
  public void deleteExerciseThrowsUserIdsDoNotMatchException() {
    when(exerciseRepository.findById(1)).thenReturn(Optional.of(exercise1));

    assertThrows(UserIdDoesNotMatchException.class, () -> {
      exerciseService.deleteExercise(1);
    });

    verify(exerciseRepository).findById(1);
  }
}
