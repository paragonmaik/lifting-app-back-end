package com.hoister.tonshoister.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoister.tonshoister.models.Program;
import com.hoister.tonshoister.services.ProgramService;

@RestController
@RequestMapping("/api/programs")
public class ProgramController {
  private final ProgramService programService;

  public ProgramController(ProgramService programService) {
    this.programService = programService;
  }

  @GetMapping
  public List<Program> getPrograms() {
    return programService.findAll();
  }
}
