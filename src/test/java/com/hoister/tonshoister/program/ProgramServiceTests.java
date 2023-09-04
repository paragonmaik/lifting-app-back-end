package com.hoister.tonshoister.program;

import static org.junit.jupiter.api.Assertions.*;
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
import com.hoister.tonshoister.advisors.UserIdDoesNotMatchException;
import com.hoister.tonshoister.models.Program;
import com.hoister.tonshoister.repositories.ProgramRepository;
import com.hoister.tonshoister.services.PrincipalService;
import com.hoister.tonshoister.services.ProgramService;

@ExtendWith(MockitoExtension.class)
public class ProgramServiceTests {

  @Mock
  PrincipalService principalService;
  @Mock
  ProgramRepository programRepository;
  @InjectMocks
  ProgramService programService;

  @Test
  public void createProgramSuccess() {
    Program program = new Program(null, null, "Starting Strength", 40, "Rookie Program.", null, null);
    String userId = "uuid";

    when(principalService.getAuthUserId()).thenReturn(userId);
    when(programRepository.save(program)).thenReturn(program);

    Program createdProgram = programService.createProgram(program);

    assertEquals(program, createdProgram);

    verify(programRepository).save(createdProgram);
  }

  @Test
  public void findAllProgramsSuccess() throws ProgramNotFoundException {
    Program program = new Program(null, null, "GVT", 12, "German Volume training", null, null);
    String userId = "uuid";
    List<Program> programs = new ArrayList<Program>();
    programs.add(program);

    when(principalService.getAuthUserId()).thenReturn(userId);
    when(programRepository.findAllByUserId(userId)).thenReturn(programs);

    List<Program> requestedPrograms = programService.findAllByUserId();

    assertEquals(programs, requestedPrograms);
    verify(programRepository).findAllByUserId(userId);
  }

  @Test
  public void findAllProgramsThrowsException() {
    List<Program> programs = new ArrayList<Program>();
    String userId = "uuid";

    when(principalService.getAuthUserId()).thenReturn(userId);
    when(programRepository.findAllByUserId(userId)).thenReturn(programs);

    assertThrows(ProgramNotFoundException.class, () -> {
      programService.findAllByUserId();
    });

    verify(programRepository).findAllByUserId(userId);
  }

  @Test
  public void findByIdSuccess() {
    Optional<Program> program = Optional.of(
        new Program(null, null, "GVT", 13, "German Volume training", null, null));

    when(programRepository.findById(1)).thenReturn(program);

    Optional<Program> requestedProgram = Optional.of(programService.findById(1));

    assertEquals(program, requestedProgram);
    verify(programRepository).findById(1);
  }

  @Test
  public void findByIdThrowsException() {
    Optional<Program> program = Optional.empty();

    when(programRepository.findById(1)).thenReturn(program);

    assertThrows(ProgramNotFoundException.class, () -> {
      programService.findById(1);
    });

    verify(programRepository).findById(1);
  }

  @Test
  public void updateProgramSuccess() throws Exception {
    String userId = "uuid";
    Program program1 = new Program(
        1, userId, "Starting Strength", 40, "Rookie Program.", null, null);
    Program program2 = new Program(1, userId, "5x5", 10, "five sets of five.", null, null);

    when(principalService.getAuthUserId()).thenReturn(userId);
    when(programRepository.findById(1)).thenReturn(Optional.of(program1));
    when(programRepository.save(program1)).thenReturn(program2);

    Program updatedProgram = programService.updateProgram(program1);

    assertNotEquals(program1.getName(), updatedProgram.getName());
    assertNotEquals(program1.getDurationWeeks(), updatedProgram.getDurationWeeks());
    assertNotEquals(program1.getDescription(), updatedProgram.getDescription());
    verify(programRepository).save(program1);
    verify(programRepository).findById(1);
  }

  @Test
  public void updateProgramThrowsException() {
    Program program = new Program(1, null, "5x5", 10, "five sets of five.", null, null);

    when(programRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(ProgramNotFoundException.class, () -> {
      programService.updateProgram(program);
    });

    verify(programRepository).findById(1);
  }

  @Test
  public void updateProgramThrowsUnauthorizedException() {
    Program program = new Program(1, "uuid", "5x5", 1, "five sets of five.", null, null);
    when(programRepository.findById(1)).thenReturn(Optional.of(program));

    assertThrows(UserIdDoesNotMatchException.class, () -> {
      programService.updateProgram(program);
    });

    verify(programRepository).findById(1);
  }

  @Test
  public void deleteProgramSuccess() throws Exception {
    Program program = new Program(1, "uuid", "5x5", 1, "five sets of five.", null, null);

    when(principalService.getAuthUserId()).thenReturn("uuid");
    when(programRepository.findById(1)).thenReturn(Optional.of(program));
    programService.deleteProgram(1);

    verify(programRepository).findById(1);
    verify(programRepository).deleteById(1);
  }

  @Test
  public void deleteProgramThrowsException() throws Exception {
    when(programRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(ProgramNotFoundException.class, () -> {
      programService.deleteProgram(1);
    });

    verify(programRepository).findById(1);
  }

  @Test
  public void deleteProgramUserIdDoesNotMatchException() {
    Program program = new Program(1, "uuid", "5x5", 1, "five sets of five.", null, null);
    when(programRepository.findById(1)).thenReturn(Optional.of(program));

    assertThrows(UserIdDoesNotMatchException.class, () -> {
      programService.deleteProgram(1);
    });

    verify(programRepository).findById(1);
  }
}
