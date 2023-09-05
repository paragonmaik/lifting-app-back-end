package com.hoister.tonshoister.program;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hoister.tonshoister.advisors.ProgramNotFoundException;
import com.hoister.tonshoister.advisors.UserIdDoesNotMatchException;
import com.hoister.tonshoister.models.Program;
import com.hoister.tonshoister.repositories.ProgramRepository;
import com.hoister.tonshoister.services.PrincipalService;
import com.hoister.tonshoister.services.ProgramService;

@ExtendWith(MockitoExtension.class)
public class ProgramServiceTests {
  private String userId = "37755df9-5607-495e-b5d4-da4f01f7c665";
  Program program1;
  Program program2;

  @Mock
  PrincipalService principalService;
  @Mock
  ProgramRepository programRepository;
  @InjectMocks
  ProgramService programService;

  @BeforeEach
  public void setEntities() {
    program1 = new Program(1, userId, "Starting Strength", 40,
        "Rookie Program.", null, null);

    program2 = new Program(1, userId, "5x5", 10,
        "five sets of five.", null, null);
  }

  @Test
  public void createProgramSuccess() {
    when(principalService.getAuthUserId()).thenReturn(userId);
    when(programRepository.save(program1)).thenReturn(program1);

    Program createdProgram = programService.createProgram(program1);

    assertEquals(createdProgram, program1);

    verify(programRepository).save(createdProgram);
  }

  @Test
  public void findAllProgramsSuccess() throws ProgramNotFoundException {
    List<Program> programs = new ArrayList<Program>();
    programs.add(program1);

    when(principalService.getAuthUserId()).thenReturn(userId);
    when(programRepository.findAllByUserId(userId)).thenReturn(programs);

    List<Program> requestedPrograms = programService.findAllByUserId();

    assertEquals(requestedPrograms, programs);
    verify(programRepository).findAllByUserId(userId);
  }

  @Test
  public void findAllProgramsThrowsException() {
    List<Program> programs = new ArrayList<Program>();

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

    assertEquals(requestedProgram, program);
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
    when(principalService.getAuthUserId()).thenReturn(userId);
    when(programRepository.findById(1)).thenReturn(Optional.of(program1));
    when(programRepository.save(program1)).thenReturn(program2);

    Program updatedProgram = programService.updateProgram(program1);

    assertNotEquals(updatedProgram.getName(), program1.getName());
    assertNotEquals(updatedProgram.getDurationWeeks(), program1.getDurationWeeks());
    assertNotEquals(updatedProgram.getDescription(), program1.getDescription());

    verify(programRepository).save(program1);
    verify(programRepository).findById(1);
  }

  @Test
  public void updateProgramThrowsException() {
    when(programRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(ProgramNotFoundException.class, () -> {
      programService.updateProgram(program1);
    });

    verify(programRepository).findById(1);
  }

  @Test
  public void updateProgramThrowsUnauthorizedException() {
    when(programRepository.findById(1)).thenReturn(Optional.of(program1));

    assertThrows(UserIdDoesNotMatchException.class, () -> {
      programService.updateProgram(program1);
    });

    verify(programRepository).findById(1);
  }

  @Test
  public void deleteProgramSuccess() throws Exception {
    when(principalService.getAuthUserId()).thenReturn(userId);
    when(programRepository.findById(1)).thenReturn(Optional.of(program1));
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
    when(programRepository.findById(1)).thenReturn(Optional.of(program1));

    assertThrows(UserIdDoesNotMatchException.class, () -> {
      programService.deleteProgram(1);
    });

    verify(programRepository).findById(1);
  }
}
