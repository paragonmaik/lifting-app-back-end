package com.hoister.tonshoister.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.hoister.tonshoister.DTOs.DTOsMapper;
import com.hoister.tonshoister.DTOs.ExerciseDTO;
import com.hoister.tonshoister.models.Exercise;
import com.hoister.tonshoister.services.ExerciseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {
  @Autowired
  private ExerciseService exerciseService;
  @Autowired
  private DTOsMapper DTOsMapper;

  @PostMapping("/{workoutId}")
  public ResponseEntity<ExerciseDTO> createExercise(
      @Valid @RequestBody ExerciseDTO exerciseDTO, @PathVariable Integer workoutId) {
    Exercise createdExercise = exerciseService
        .createExercise(DTOsMapper.convertToEntity(exerciseDTO), workoutId);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(DTOsMapper.convertToDto(createdExercise));
  }

  @GetMapping
  public List<ExerciseDTO> getExercises() {
    return exerciseService.findAllByUserId()
        .stream().map(exercise -> DTOsMapper.convertToDto(exercise)).toList();
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PutMapping
  public void updateExercise(@Valid @RequestBody ExerciseDTO exerciseDTO) {
    exerciseService.updateExercise(DTOsMapper.convertToEntity(exerciseDTO));
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{id}")
  public void deleteExercise(@PathVariable Integer id) {
    exerciseService.deleteExercise(id);
  }
}
