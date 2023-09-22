package com.hoister.tonshoister.DTOs;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.hoister.tonshoister.models.Exercise;
import com.hoister.tonshoister.models.Profile;
import com.hoister.tonshoister.models.Program;
import com.hoister.tonshoister.models.User;
import com.hoister.tonshoister.models.Workout;

@Service
public class DTOsMapper {

  // Programs
  public ProgramDTO convertToDto(Program program) {
    Set<WorkoutDTO> workouts = new HashSet<>();
    if (program.getWorkouts() != null) {
      for (Workout workout : program.getWorkouts()) {
        workouts.add(convertToDto(workout));
      }
    }

    return new ProgramDTO(
        program.getId(),
        program.getUserId(),
        program.getName(),
        program.getDurationWeeks(),
        program.getDescription(),
        program.getDateCreated(),
        workouts);
  }

  public Program convertToEntity(ProgramDTO programDTO) {
    return new Program(
        programDTO.id(),
        programDTO.userId(),
        programDTO.name(),
        programDTO.durationWeeks(),
        programDTO.description(),
        programDTO.dateCreated(),
        new HashSet<Workout>());
  }

  // Workouts
  public WorkoutDTO convertToDto(Workout workout) {
    Set<ExerciseDTO> exercises = new HashSet<>();
    if (workout.getExercises() != null) {
      for (Exercise exercise : workout.getExercises()) {
        exercises.add(convertToDto(exercise));
      }
    }

    return new WorkoutDTO(
        workout.getId(),
        workout.getUserId(),
        workout.getName(),
        workout.getDurationMins(),
        workout.getDescription(),
        workout.getDateCreated(),
        exercises);
  }

  public Workout convertToEntity(WorkoutDTO workoutDTO) {
    return new Workout(
        workoutDTO.id(),
        workoutDTO.userId(),
        workoutDTO.name(),
        workoutDTO.durationMins(),
        workoutDTO.description(),
        workoutDTO.dateCreated(),
        new HashSet<Program>(),
        new HashSet<Exercise>());
  }

  // Exercises
  public ExerciseDTO convertToDto(Exercise exercise) {
    return new ExerciseDTO(
        exercise.getId(),
        exercise.getUserId(),
        exercise.getName(),
        exercise.getLoad(),
        exercise.getGoal(),
        exercise.getRestSeconds(),
        exercise.getInstructions(),
        exercise.getDateCreated(),
        exercise.getSets(),
        exercise.getReps());
  }

  public Exercise convertToEntity(ExerciseDTO exerciseDTO) {
    return new Exercise(
        exerciseDTO.id(),
        exerciseDTO.userId(),
        exerciseDTO.name(),
        exerciseDTO.load(),
        exerciseDTO.goal(),
        exerciseDTO.restSeconds(),
        exerciseDTO.instructions(),
        exerciseDTO.dateCreated(),
        exerciseDTO.sets(),
        exerciseDTO.reps(),
        new HashSet<Workout>());
  }

  // Authentication
  public User convertToEntity(RegisterDTO registerDTO) {
    return new User(
        registerDTO.login(),
        registerDTO.password(),
        registerDTO.role());
  }

  // Profile
  public Profile convertToEntity(ProfileDTO profileDTO) {
    return new Profile(
        profileDTO.id(),
        profileDTO.weight(),
        profileDTO.height(),
        profileDTO.user(),
        new HashSet<Program>(),
        new HashSet<Workout>(),
        new HashSet<Exercise>());
  }
}
