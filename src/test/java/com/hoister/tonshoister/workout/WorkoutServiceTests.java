package com.hoister.tonshoister.workout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
public class WorkoutServiceTests {
  private String userId = "37755df9-5607-495e-b5d4-da4f01f7c665";
  Workout workout1;
  Workout workout2;
  Program program;

  @Mock
  PrincipalService principalService;
  @Mock
  WorkoutRepository workoutRepository;
  @Mock
  ProgramRepository programRepository;
  @InjectMocks
  WorkoutService workoutService;

  @BeforeEach
  public void setEntities() {
    workout1 = new Workout(
        1, userId, "Workout A", 20, "Rookie workout.",
        null, new HashSet<Program>(), new HashSet<>());
    workout2 = new Workout(
        1, userId, "Workout B", 10, "Intense workout.",
        null, new HashSet<Program>(), new HashSet<Exercise>());
    program = new Program(1, userId, "5x5", 52,
        null, null, new HashSet<Workout>());
  }

  @Test
  public void createWorkoutSuccess() {
    when(principalService.getAuthUserId()).thenReturn(userId);
    when(programRepository.findById(1)).thenReturn(Optional.of(program));
    when(workoutRepository.save(workout1)).thenReturn(workout1);

    Workout createdWorkout = workoutService.createWorkout(workout1, 1);

    assertEquals(createdWorkout, workout1);

    verify(workoutRepository, times(2)).save(createdWorkout);
  }

  @Test
  public void createWorkoutThrowsException() throws ProgramNotFoundException {
    when(programRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(ProgramNotFoundException.class, () -> {
      workoutService.createWorkout(workout1, 1);
    });

    verify(programRepository).findById(1);
  }

  @Test
  public void findAllWorkoutsSuccess() throws WorkoutNotFoundException {
    List<Workout> workouts = new ArrayList<Workout>();
    workouts.add(workout1);

    when(principalService.getAuthUserId()).thenReturn(userId);
    when(workoutRepository.findAllByUserId(userId)).thenReturn(workouts);

    List<Workout> requestedWorkouts = workoutService.findAllByUserId();

    assertEquals(requestedWorkouts, workouts);
    verify(workoutRepository).findAllByUserId(userId);
  }

  @Test
  public void findAllWorkoutsThrowsException() throws WorkoutNotFoundException {
    List<Workout> workouts = new ArrayList<Workout>();

    when(principalService.getAuthUserId()).thenReturn(userId);
    when(workoutRepository.findAllByUserId(userId)).thenReturn(workouts);

    assertThrows(WorkoutNotFoundException.class, () -> {
      workoutService.findAllByUserId();
    });

    verify(workoutRepository).findAllByUserId(userId);
  }

  @Test
  public void updateWorkoutSuccess() throws WorkoutNotFoundException {
    when(principalService.getAuthUserId()).thenReturn(userId);
    when(workoutRepository.findById(1)).thenReturn(Optional.of(workout1));
    when(workoutRepository.save(workout1)).thenReturn(workout2);

    Workout updatedWorkout = workoutService.updateWorkout(workout1);

    assertNotEquals(updatedWorkout.getName(), workout1.getName());
    assertNotEquals(updatedWorkout.getDurationMins(), workout1.getDurationMins());
    assertNotEquals(updatedWorkout.getDescription(), workout1.getDescription());

    verify(workoutRepository).save(workout1);
    verify(workoutRepository).findById(1);
  }

  @Test
  public void updateWorkoutThrowsException() throws WorkoutNotFoundException {
    when(workoutRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(WorkoutNotFoundException.class, () -> {
      workoutService.updateWorkout(workout1);
    });

    verify(workoutRepository).findById(1);
  }

  @Test
  public void updateWorkoutThrowsUserIdDoesNotMatchException() throws WorkoutNotFoundException {
    when(workoutRepository.findById(1)).thenReturn(Optional.of(workout1));

    assertThrows(UserIdDoesNotMatchException.class, () -> {
      workoutService.updateWorkout(workout1);
    });

    verify(workoutRepository).findById(1);
  }

  @Test
  public void deleteWorkoutSuccess() throws Exception {
    when(principalService.getAuthUserId()).thenReturn(userId);
    when(workoutRepository.findById(1)).thenReturn(Optional.of(workout1));
    workoutService.deleteWorkout(1);

    verify(workoutRepository).findById(1);
    verify(workoutRepository).deleteById(1);
  }

  @Test
  public void deleteWorkoutThrowsException() throws Exception {
    when(workoutRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(WorkoutNotFoundException.class, () -> {
      workoutService.deleteWorkout(1);
    });

    verify(workoutRepository).findById(1);
  }

  @Test
  public void deleteProgramThrowsUserIdsDoNotMatchException() throws Exception {
    when(workoutRepository.findById(1)).thenReturn(Optional.of(workout1));

    assertThrows(UserIdDoesNotMatchException.class, () -> {
      workoutService.deleteWorkout(1);
    });

    verify(workoutRepository).findById(1);
  }
}
