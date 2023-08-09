package com.hoister.tonshoister.services;

import org.springframework.stereotype.Service;

import com.hoister.tonshoister.repositories.ProgramRepository;

@Service
public class ProgramService {
  private final ProgramRepository programRepository;

  public ProgramService(ProgramRepository programRepository) {
    this.programRepository = programRepository;
  }

}
