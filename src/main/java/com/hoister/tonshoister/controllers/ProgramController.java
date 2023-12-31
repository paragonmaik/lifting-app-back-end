package com.hoister.tonshoister.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.hoister.tonshoister.DTOs.DTOsMapper;
import com.hoister.tonshoister.DTOs.ProgramDTO;
import com.hoister.tonshoister.models.Program;
import com.hoister.tonshoister.services.ProgramService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/programs")
public class ProgramController {

  @Autowired
  private ProgramService programService;
  @Autowired
  private DTOsMapper DTOsMapper;

  @PostMapping
  public ResponseEntity<ProgramDTO> createProgram(@Valid @RequestBody ProgramDTO programDTO) {
    Program createdProgram = programService
        .createProgram(DTOsMapper.convertToEntity(programDTO));

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(DTOsMapper.convertToDto(createdProgram));
  }

  @GetMapping
  public List<ProgramDTO> getPrograms() {
    return programService.findAllByUserId()
        .stream().map(program -> DTOsMapper.convertToDto(program)).toList();
  }

  @GetMapping("/temp/{id}")
  public ProgramDTO getProgramById(@PathVariable Integer id) {
    return DTOsMapper.convertToDto(programService.findById(id));
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PutMapping
  public void updateProgram(@Valid @RequestBody ProgramDTO programDTO) {
    programService.updateProgram(DTOsMapper.convertToEntity(programDTO));
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{id}")
  public void deleteProgram(@PathVariable Integer id) {
    programService.deleteProgram(id);
  }
}
