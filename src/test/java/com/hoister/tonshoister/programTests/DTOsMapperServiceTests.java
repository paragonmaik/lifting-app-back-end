package com.hoister.tonshoister.programTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.hoister.tonshoister.DTOs.DTOsMapper;
import com.hoister.tonshoister.DTOs.ProgramDTO;
import com.hoister.tonshoister.DTOs.WorkoutDTO;
import com.hoister.tonshoister.models.Program;
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
        1, "Squat Everyday", 52, "Keep squatting.", LocalDateTime.now(), workouts);

    ProgramDTO programDTO = DTOsMapper.convertToDto(program);

    assertEquals(program.getId(), programDTO.id());
    assertEquals(program.getName(), programDTO.name());
    assertEquals(program.getDurationWeeks(), programDTO.durationWeeks());
    assertEquals(program.getDescription(), programDTO.description());
    assertEquals(program.getDateCreated(), programDTO.dateCreated());
    assertEquals(program.getWorkouts(), programDTO.workouts());

    assertInstanceOf(ProgramDTO.class, programDTO);
  }

  @Test
  public void convertDTOToProgram() {
    ProgramDTO programDTO = new ProgramDTO(null, "Squat Everyday", 10,
        "A tough program.", null, null);

    Program program = DTOsMapper.convertToEntity(programDTO);

    assertEquals(program.getName(), programDTO.name());
    assertEquals(program.getDurationWeeks(), programDTO.durationWeeks());
    assertEquals(program.getDescription(), programDTO.description());

    assertInstanceOf(Program.class, program);
  }

  @Test
  public void convertWorkoutToDTO() {
    Workout workout = new Workout(1, "Workout A", 70, "Long workout.", LocalDateTime.now());

    WorkoutDTO workoutDTO = DTOsMapper.convertToDto(workout);

    assertEquals(workout.getId(), workoutDTO.id());
    assertEquals(workout.getName(), workoutDTO.name());
    assertEquals(workout.getDurationMins(), workoutDTO.durationMins());
    assertEquals(workout.getDescription(), workoutDTO.description());
    assertEquals(workout.getDateCreated(), workoutDTO.dateCreated());

    assertInstanceOf(WorkoutDTO.class, workoutDTO);
  }

  @Test
  public void convertDTOtoWorkout() {
    WorkoutDTO workoutDTO = new WorkoutDTO(null, "Workout A", 70,
        "A really long workout", null);

    Workout workout = DTOsMapper.convertToEntity(workoutDTO);

    assertEquals(workout.getName(), workoutDTO.name());
    assertEquals(workout.getDurationMins(), workoutDTO.durationMins());
    assertEquals(workout.getDescription(), workoutDTO.description());

    assertInstanceOf(Workout.class, workout);
  }
}
