package com.hoister.tonshoister.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoister.tonshoister.models.Program;
import com.hoister.tonshoister.services.ProgramService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/programs")
public class ProgramController {
  private final ProgramService programService;

  public ProgramController(ProgramService programService) {
    this.programService = programService;
  }

  @PostMapping("")
  public ResponseEntity<Program> createProgram(@Valid @RequestBody Program program) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(programService.createProgram(program));
  }

  @GetMapping
  public List<Program> getPrograms() {
    return programService.findAll();
  }

  @GetMapping("/{id}")
  public Program getProgramById(@PathVariable Integer id) {
    return programService.findById(id);
  }
}
