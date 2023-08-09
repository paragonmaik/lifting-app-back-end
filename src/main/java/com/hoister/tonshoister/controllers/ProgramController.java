package com.hoister.tonshoister.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoister.tonshoister.services.ProgramService;

@RestController
@RequestMapping("/api/program")
public class ProgramController {
  private final ProgramService programService;

  public ProgramController(ProgramService programService) {
    this.programService = programService;
  }

}
