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
    for (Workout workout : program.getWorkouts()) {
      workouts.add(convertToDto(workout));
    }

    return new ProgramDTO(
        program.getId(),
        program.getName(),
        program.getDurationWeeks(),
        program.getDescription(),
        program.getDateCreated(),
        workouts);
  }

  public Program convertToEntity(ProgramDTO programDTO) {
    return new Program(
        programDTO.id(),
        programDTO.name(),
        programDTO.durationWeeks(),
        programDTO.description());
  }

  // Workouts
  public WorkoutDTO convertToDto(Workout workout) {
    Set<ExerciseDTO> exercises = new HashSet<>();
    for (Exercise exercise : workout.getExercises()) {
      exercises.add(convertToDto(exercise));
    }

    return new WorkoutDTO(
        workout.getId(),
        workout.getName(),
        workout.getDurationMins(),
        workout.getDescription(),
        workout.getDateCreated(),
        exercises);
  }

  public Workout convertToEntity(WorkoutDTO workoutDTO) {
    return new Workout(
        workoutDTO.id(),
        workoutDTO.name(),
        workoutDTO.durationMins(),
        workoutDTO.description());
  }

  // Exercises
  public ExerciseDTO convertToDto(Exercise exercise) {
    return new ExerciseDTO(
        exercise.getId(),
        exercise.getName(),
        exercise.getLoad(),
        exercise.getGoal(),
        exercise.getRestSeconds(),
        exercise.getInstructions(),
        exercise.getDateCreated());
  }

  public Exercise convertToEntity(ExerciseDTO exerciseDTO) {
    return new Exercise(
        exerciseDTO.id(),
        exerciseDTO.name(),
        exerciseDTO.load(),
        exerciseDTO.goal(),
        exerciseDTO.restSeconds(),
        exerciseDTO.instructions());
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
        profileDTO.weight(),
        profileDTO.height());
  }
}
