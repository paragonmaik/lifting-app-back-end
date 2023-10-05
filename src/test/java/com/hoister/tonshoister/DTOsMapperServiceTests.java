package com.hoister.tonshoister;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.*;

import com.hoister.tonshoister.DTOs.*;
import com.hoister.tonshoister.models.*;

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
        1, null, "Squat Everyday", 52, "Keep squatting.", LocalDateTime.now(), workouts);

    ProgramDTO programDTO = DTOsMapper.convertToDto(program);

    assertEquals(programDTO.id(), program.getId());
    assertEquals(programDTO.userId(), program.getUserId());
    assertEquals(programDTO.name(), program.getName());
    assertEquals(programDTO.durationWeeks(), program.getDurationWeeks());
    assertEquals(programDTO.description(), program.getDescription());
    assertEquals(programDTO.dateCreated(), program.getDateCreated());
    assertEquals(programDTO.workouts(), program.getWorkouts());

    assertInstanceOf(ProgramDTO.class, programDTO);
  }

  @Test
  public void convertDTOToProgram() {
    ProgramDTO programDTO = new ProgramDTO(null, null, "Squat Everyday", 10,
        "A tough program.", null, null);

    Program program = DTOsMapper.convertToEntity(programDTO);

    assertEquals(programDTO.name(), program.getName());
    assertEquals(programDTO.durationWeeks(), program.getDurationWeeks());
    assertEquals(programDTO.description(), program.getDescription());

    assertInstanceOf(Program.class, program);
  }

  @Test
  public void convertWorkoutToDTO() {
    Set<Exercise> exercises = new HashSet<>();
    Workout workout = new Workout(
        1, null, "Workout A", 70, "Long workout.", LocalDateTime.now(), null, exercises);

    WorkoutDTO workoutDTO = DTOsMapper.convertToDto(workout);

    assertEquals(workoutDTO.id(), workout.getId());
    assertEquals(workoutDTO.name(), workout.getName());
    assertEquals(workoutDTO.durationMins(), workout.getDurationMins());
    assertEquals(workoutDTO.description(), workout.getDescription());
    assertEquals(workoutDTO.dateCreated(), workout.getDateCreated());

    assertInstanceOf(WorkoutDTO.class, workoutDTO);
  }

  @Test
  public void convertDTOToWorkout() {
    WorkoutDTO workoutDTO = new WorkoutDTO(null, "userid", "Workout A", 70,
        "A really long workout", null, null);

    Workout workout = DTOsMapper.convertToEntity(workoutDTO);

    assertEquals(workoutDTO.name(), workout.getName());
    assertEquals(workoutDTO.durationMins(), workout.getDurationMins());
    assertEquals(workoutDTO.description(), workout.getDescription());

    assertInstanceOf(Workout.class, workout);
  }

  @Test
  public void convertExerciseToDTO() {
    Exercise exercise = new Exercise(
        1, "uuid", "High Bar Back Squat", 210, GoalType.STRENGTH, 150,
        "Squat deep.", LocalDateTime.now(), 3, 5, 1, null);

    ExerciseDTO exerciseDTO = DTOsMapper.convertToDto(exercise);

    assertEquals(exerciseDTO.id(), exercise.getId());
    assertEquals(exerciseDTO.name(), exercise.getName());
    assertEquals(exerciseDTO.load(), exercise.getLoad());
    assertEquals(exerciseDTO.goal(), exercise.getGoal());
    assertEquals(exerciseDTO.restSeconds(), exercise.getRestSeconds());
    assertEquals(exerciseDTO.instructions(), exercise.getInstructions());
    assertEquals(exerciseDTO.dateCreated(), exercise.getDateCreated());
    assertEquals(exerciseDTO.reps(), exercise.getReps());
    assertEquals(exerciseDTO.sets(), exercise.getSets());

    assertInstanceOf(ExerciseDTO.class, exerciseDTO);
  }

  @Test
  public void convertDTOToExercise() {
    ExerciseDTO exerciseDTO = new ExerciseDTO(
        1, null, "High Bar Back Squat", 210, GoalType.STRENGTH, 150,
        "Squat deep.", LocalDateTime.now(), 3, 5, 1);

    Exercise exercise = DTOsMapper.convertToEntity(exerciseDTO);

    assertEquals(exerciseDTO.name(), exercise.getName());
    assertEquals(exerciseDTO.load(), exercise.getLoad());
    assertEquals(exerciseDTO.goal(), exercise.getGoal());
    assertEquals(exerciseDTO.restSeconds(), exercise.getRestSeconds());
    assertEquals(exerciseDTO.instructions(), exercise.getInstructions());
    assertEquals(exerciseDTO.reps(), exercise.getReps());
    assertEquals(exerciseDTO.sets(), exercise.getSets());

    assertInstanceOf(Exercise.class, exercise);
  }

  @Test
  public void convertDTOToUser() {
    RegisterDTO registerDTO = new RegisterDTO(
        "arnold", "gettothechoppah", UserRole.ADMIN);
    User user = DTOsMapper.convertToEntity(registerDTO);

    assertEquals(registerDTO.login(), user.getLogin());
    assertEquals(registerDTO.password(), user.getPassword());
    assertEquals(registerDTO.role(), user.getRole());
  }

  @Test
  public void convertDTOToProfile() {
    ProfileDTO profileDTO = new ProfileDTO(
        "37755df9-5607-495e-b5d4-da4f01f7c665", 75, 175, null);
    Profile profile = DTOsMapper.convertToEntity(profileDTO);

    assertEquals(profileDTO.id(), profile.getId());
    assertEquals(profileDTO.weight(), profile.getWeight());
    assertEquals(profileDTO.height(), profile.getHeight());
  }
}
