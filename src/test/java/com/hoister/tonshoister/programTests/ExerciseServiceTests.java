package com.hoister.tonshoister.programTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.hoister.tonshoister.advisors.WorkoutNotFoundException;
import com.hoister.tonshoister.models.Exercise;
import com.hoister.tonshoister.models.GoalType;
import com.hoister.tonshoister.models.Workout;
import com.hoister.tonshoister.repositories.ExerciseRepository;
import com.hoister.tonshoister.repositories.WorkoutRepository;
import com.hoister.tonshoister.services.ExerciseService;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceTests {

  @Mock
  ExerciseRepository exerciseRepository;
  @Mock
  WorkoutRepository workoutRepository;
  @InjectMocks
  ExerciseService exerciseService;

  @Test
  public void createExerciseSuccess() throws WorkoutNotFoundException {
    Exercise exercise = new Exercise(
        "High Bar Squat", 120, GoalType.STRENGTH, 150, "Bar rests at the traps.");
    Workout workout = new Workout("Workout A", 10, "A really tough workout.");

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
}
