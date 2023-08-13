package com.hoister.tonshoister.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hoister.tonshoister.advisors.ProgramNotFoundException;
import com.hoister.tonshoister.models.Program;
import com.hoister.tonshoister.repositories.ProgramRepository;

@Service
public class ProgramService {
  private final ProgramRepository programRepository;

  public ProgramService(ProgramRepository programRepository) {
    this.programRepository = programRepository;
  }

  public Program createProgram(Program program) {
    return programRepository.save(program);
  }

  public List<Program> findAll() throws ProgramNotFoundException {
    List<Program> programsList = programRepository.findAll();

    if (programsList.isEmpty()) {
      throw new ProgramNotFoundException();
    }

    return programsList;
  }

  public Program findById(Integer id) throws ProgramNotFoundException {
    return programRepository.findById(id).orElseThrow(() -> new ProgramNotFoundException());
  }

  public Program updateProgram(Program program) throws ProgramNotFoundException {
    if (!programRepository.existsById(program.getId())) {
      throw new ProgramNotFoundException();
    }

    return programRepository.save(program);
  }
}
