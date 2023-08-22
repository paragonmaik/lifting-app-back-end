package com.hoister.tonshoister.workout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hoister.tonshoister.advisors.ProgramNotFoundException;
import com.hoister.tonshoister.advisors.WorkoutNotFoundException;
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

  @Test
  public void findAllWorkoutsSuccess() throws WorkoutNotFoundException {
    Workout workout = new Workout("Workout A", 12, "A really boring one.");
    List<Workout> workouts = new ArrayList<Workout>();
    workouts.add(workout);

    when(workoutRepository.findAll()).thenReturn(workouts);

    List<Workout> requestedWorkouts = workoutRepository.findAll();

    assertEquals(workouts, requestedWorkouts);
    verify(workoutRepository).findAll();
  }

  @Test
  public void findAllWorkoutsThrowsException() throws WorkoutNotFoundException {
    List<Workout> workouts = new ArrayList<Workout>();

    when(workoutRepository.findAll()).thenReturn(workouts);

    assertThrows(WorkoutNotFoundException.class, () -> {
      workoutService.findAll();
    });

    verify(workoutRepository).findAll();

  }

  @Test
  public void updateWorkoutSuccess() throws WorkoutNotFoundException {
    Workout workout1 = new Workout(
        1, "Workout A", 20, "Rookie workout.", null, null);
    Workout workout2 = new Workout(
        1, "Workout B", 10, "Intense workout.", null, null);

    when(workoutRepository.findById(1)).thenReturn(Optional.of(workout1));
    when(workoutRepository.save(workout1)).thenReturn(workout2);

    Workout updatedWorkout = workoutService.updateWorkout(workout1);

    assertNotEquals(workout1.getName(), updatedWorkout.getName());
    assertNotEquals(workout1.getDurationMins(), updatedWorkout.getDurationMins());
    assertNotEquals(workout1.getDescription(), updatedWorkout.getDescription());

    verify(workoutRepository).save(workout1);
    verify(workoutRepository).findById(1);
  }

  @Test
  public void updateWorkoutThrowsException() throws WorkoutNotFoundException {
    Workout workout = new Workout(
        1, "Workout A", 20, "Rookie workout.", null, null);

    when(workoutRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(WorkoutNotFoundException.class, () -> {
      workoutService.updateWorkout(workout);
    });

    verify(workoutRepository).findById(1);
  }

  @Test
  public void deleteWorkoutSuccess() throws Exception {
    when(workoutRepository.existsById(1)).thenReturn(true);
    workoutService.deleteWorkout(1);
    
    verify(workoutRepository).existsById(1);
    verify(workoutRepository).deleteById(1);
  }

  @Test
  public void deleteWorkoutThrowsException() throws Exception {
    when(workoutRepository.existsById(1)).thenReturn(false);

    assertThrows(WorkoutNotFoundException.class, () -> {
      workoutService.deleteWorkout(1);
    });

    verify(workoutRepository).existsById(1);
  }
}
