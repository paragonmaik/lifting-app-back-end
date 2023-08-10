package com.hoister.tonshoister.programTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import com.hoister.tonshoister.models.Program;
import com.hoister.tonshoister.repositories.ProgramRepository;
import com.hoister.tonshoister.services.ProgramService;

@ExtendWith(MockitoExtension.class)
public class ProgramServiceTests {

  @Mock
  ProgramRepository programRepository;
  @InjectMocks
  ProgramService programService;

  @Test
  public void findAllProgramsSuccess() throws ProgramNotFoundException {
    Program program = new Program("GVT", 12, "German Volume training");
    List<Program> programs = new ArrayList<Program>();
    programs.add(program);

    when(programRepository.findAll()).thenReturn(programs);

    List<Program> requestedPrograms = programService.findAll();

    assertEquals(programs, requestedPrograms);
    verify(programRepository).findAll();
  }

  @Test
  public void findAllProgramsThrowsException() {
    List<Program> programs = new ArrayList<Program>();

    when(programRepository.findAll()).thenReturn(programs);

    assertThrows(ProgramNotFoundException.class, () -> {
      programService.findAll();
    });

    verify(programRepository).findAll();
  }

  @Test
  public void findByIdSuccess() {
    Optional<Program> program = Optional.of(
        new Program("GVT", 12, "German Volume training"));

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
}
