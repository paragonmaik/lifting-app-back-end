package com.hoister.tonshoister.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hoister.tonshoister.DTOs.DTOsMapper;
import com.hoister.tonshoister.DTOs.ExerciseDTO;
import com.hoister.tonshoister.models.Exercise;
import com.hoister.tonshoister.services.ExerciseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {
  private final ExerciseService exerciseService;
  private final DTOsMapper DTOsMapper;

  public ExerciseController(ExerciseService exerciseService,
      DTOsMapper DTOsMapper) {
    this.exerciseService = exerciseService;
    this.DTOsMapper = DTOsMapper;
  }

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
    return exerciseService.findAll()
        .stream().map(exercise -> DTOsMapper.convertToDto(exercise)).toList();
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PutMapping
  public void updateExercise(@Valid @RequestBody ExerciseDTO exerciseDTO) {
    exerciseService.updateExercise(DTOsMapper.convertToEntity(exerciseDTO));
  }
}
