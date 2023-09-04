package com.hoister.tonshoister;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.hoister.tonshoister.DTOs.DTOsMapper;
import com.hoister.tonshoister.DTOs.ExerciseDTO;
import com.hoister.tonshoister.DTOs.ProfileDTO;
import com.hoister.tonshoister.DTOs.ProgramDTO;
import com.hoister.tonshoister.DTOs.RegisterDTO;
import com.hoister.tonshoister.DTOs.WorkoutDTO;
import com.hoister.tonshoister.models.Exercise;
import com.hoister.tonshoister.models.GoalType;
import com.hoister.tonshoister.models.Profile;
import com.hoister.tonshoister.models.Program;
import com.hoister.tonshoister.models.User;
import com.hoister.tonshoister.models.UserRole;
import com.hoister.tonshoister.models.Workout;

public class DTOsMapperServiceTests {
  private DTOsMapper DTOsMapper;

  @BeforeEach
  public void init() {
    DTOsMapper = new DTOsMapper();
  }

  @Test
  public void convertProgramToDTO() {
    Set<Workout> workouts = new HashSet<>();
    Program program = new Program(
        1, "uuid", "Squat Everyday", 52, "Keep squatting.", LocalDateTime.now(), workouts);

    ProgramDTO programDTO = DTOsMapper.convertToDto(program);

    assertEquals(program.getId(), programDTO.id());
    assertEquals(program.getUserId(), programDTO.userId());
    assertEquals(program.getName(), programDTO.name());
    assertEquals(program.getDurationWeeks(), programDTO.durationWeeks());
    assertEquals(program.getDescription(), programDTO.description());
    assertEquals(program.getDateCreated(), programDTO.dateCreated());
    assertEquals(program.getWorkouts(), programDTO.workouts());

    assertInstanceOf(ProgramDTO.class, programDTO);
  }

  @Test
  public void convertDTOToProgram() {
    ProgramDTO programDTO = new ProgramDTO(null, null, "Squat Everyday", 10,
        "A tough program.", null, null);

    Program program = DTOsMapper.convertToEntity(programDTO);

    assertEquals(program.getName(), programDTO.name());
    assertEquals(program.getDurationWeeks(), programDTO.durationWeeks());
    assertEquals(program.getDescription(), programDTO.description());

    assertInstanceOf(Program.class, program);
  }

  @Test
  public void convertWorkoutToDTO() {
    Set<Exercise> exercises = new HashSet<>();
    Workout workout = new Workout(
        1, "Workout A", 70, "Long workout.", LocalDateTime.now(), exercises);

    WorkoutDTO workoutDTO = DTOsMapper.convertToDto(workout);

    assertEquals(workout.getId(), workoutDTO.id());
    assertEquals(workout.getName(), workoutDTO.name());
    assertEquals(workout.getDurationMins(), workoutDTO.durationMins());
    assertEquals(workout.getDescription(), workoutDTO.description());
    assertEquals(workout.getDateCreated(), workoutDTO.dateCreated());

    assertInstanceOf(WorkoutDTO.class, workoutDTO);
  }

  @Test
  public void convertDTOToWorkout() {
    WorkoutDTO workoutDTO = new WorkoutDTO(null, "userid", "Workout A", 70,
        "A really long workout", null, null);

    Workout workout = DTOsMapper.convertToEntity(workoutDTO);

    assertEquals(workout.getName(), workoutDTO.name());
    assertEquals(workout.getDurationMins(), workoutDTO.durationMins());
    assertEquals(workout.getDescription(), workoutDTO.description());

    assertInstanceOf(Workout.class, workout);
  }

  @Test
  public void convertExerciseToDTO() {
    Exercise exercise = new Exercise(1, "High Bar Back Squat", 210, GoalType.STRENGTH, 150,
        "Squat deep.", LocalDateTime.now());

    ExerciseDTO exerciseDTO = DTOsMapper.convertToDto(exercise);

    assertEquals(exercise.getId(), exerciseDTO.id());
    assertEquals(exercise.getName(), exerciseDTO.name());
    assertEquals(exercise.getLoad(), exerciseDTO.load());
    assertEquals(exercise.getGoal(), exerciseDTO.goal());
    assertEquals(exercise.getRestSeconds(), exerciseDTO.restSeconds());
    assertEquals(exercise.getInstructions(), exerciseDTO.instructions());
    assertEquals(exercise.getDateCreated(), exerciseDTO.dateCreated());

    assertInstanceOf(ExerciseDTO.class, exerciseDTO);
  }

  @Test
  public void convertDTOToExercise() {
    ExerciseDTO exerciseDTO = new ExerciseDTO(
        1, "userid", "High Bar Back Squat", 210, GoalType.STRENGTH, 150,
        "Squat deep.", LocalDateTime.now());

    Exercise exercise = DTOsMapper.convertToEntity(exerciseDTO);

    assertEquals(exercise.getName(), exerciseDTO.name());
    assertEquals(exercise.getLoad(), exerciseDTO.load());
    assertEquals(exercise.getGoal(), exerciseDTO.goal());
    assertEquals(exercise.getRestSeconds(), exerciseDTO.restSeconds());
    assertEquals(exercise.getInstructions(), exerciseDTO.instructions());

    assertInstanceOf(Exercise.class, exercise);
  }

  @Test
  public void convertDTOToUser() {
    RegisterDTO registerDTO = new RegisterDTO(
        "arnold", "gettothechoppah", UserRole.ADMIN);
    User user = DTOsMapper.convertToEntity(registerDTO);

    assertEquals(user.getLogin(), registerDTO.login());
    assertEquals(user.getPassword(), registerDTO.password());
    assertEquals(user.getRole(), registerDTO.role());
  }

  @Test
  public void convertDTOToProfile() {
    ProfileDTO profileDTO = new ProfileDTO(
        "uuid", 75, 175, null);
    Profile profile = DTOsMapper.convertToEntity(profileDTO);

    assertEquals(profile.getId(), profileDTO.id());
    assertEquals(profile.getWeight(), profileDTO.weight());
    assertEquals(profile.getHeight(), profileDTO.height());
  }
}
