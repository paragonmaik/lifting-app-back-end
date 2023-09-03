package com.hoister.tonshoister.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoister.tonshoister.advisors.ProgramNotFoundException;
import com.hoister.tonshoister.advisors.UserIdDoesNotMatchException;
import com.hoister.tonshoister.models.Program;
import com.hoister.tonshoister.repositories.ProgramRepository;

@Service
public class ProgramService {

  @Autowired
  private PrincipalService principalService;
  @Autowired
  private ProgramRepository programRepository;

  public Program createProgram(Program program) {
    program.setUserId(principalService.getAuthUserId());
    return programRepository.save(program);
  }

  public List<Program> findAllByUserId() throws ProgramNotFoundException {
    List<Program> programsList = programRepository
        .findAllByUserId(principalService.getAuthUserId());

    if (programsList.isEmpty()) {
      throw new ProgramNotFoundException();
    }

    return programsList;
  }

  public Program findById(Integer id) throws ProgramNotFoundException {
    return programRepository.findById(id).orElseThrow(() -> new ProgramNotFoundException());
  }

  public Program updateProgram(Program program) throws ProgramNotFoundException {
    Program foundProgram = programRepository.findById(program.getId())
        .orElseThrow(() -> new ProgramNotFoundException());

    if (!foundProgram.getUserId().equals(principalService.getAuthUserId())) {
      throw new UserIdDoesNotMatchException();
    }

    foundProgram.setName(program.getName());
    foundProgram.setDescription(program.getDescription());
    foundProgram.setDurationWeeks(program.getDurationWeeks());

    return programRepository.save(foundProgram);
  }

  public void deleteProgram(Integer id) throws ProgramNotFoundException {
    Program foundProgram = programRepository.findById(id)
        .orElseThrow(() -> new ProgramNotFoundException());

    if (!foundProgram.getUserId().equals(principalService.getAuthUserId())) {
      throw new UserIdDoesNotMatchException();
    }
    programRepository.deleteById(id);
  }
}
