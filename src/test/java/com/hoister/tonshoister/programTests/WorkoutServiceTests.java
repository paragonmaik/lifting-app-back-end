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

import com.hoister.tonshoister.advisors.ProgramNotFoundException;
import com.hoister.tonshoister.models.Program;
import com.hoister.tonshoister.models.Workout;
import com.hoister.tonshoister.repositories.ProgramRepository;
import com.hoister.tonshoister.repositories.WorkoutRepository;
import com.hoister.tonshoister.services.WorkoutService;

@ExtendWith(MockitoExtension.class)
public class WorkoutServiceTests {

  @Mock
  WorkoutRepository workoutRepository;
  @Mock
  ProgramRepository programRepository;
  @InjectMocks
  WorkoutService workoutService;

  @Test
  public void createWorkoutSuccess() {
    Workout workout = new Workout("Workout A", 10, "A really tough workout.");
    Program program = new Program(1, "5x5", 52, null);

    when(programRepository.findById(1)).thenReturn(Optional.of(program));
    when(workoutRepository.save(workout)).thenReturn(workout);

    Workout createdWorkout = workoutService.createWorkout(workout, 1);

    assertEquals(workout, createdWorkout);

    verify(workoutRepository, times(2)).save(createdWorkout);
  }

  @Test
  public void createWorkoutThrowsException() throws ProgramNotFoundException {
    Workout workout = new Workout("Workout A", 10, "A really tough workout.");

    when(programRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(ProgramNotFoundException.class, () -> {
      workoutService.createWorkout(workout, 1);
    });

    verify(programRepository).findById(1);
  }
}
