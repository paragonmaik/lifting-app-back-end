package com.hoister.tonshoister.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoister.tonshoister.DTOs.DTOsMapper;
import com.hoister.tonshoister.DTOs.WorkoutDTO;
import com.hoister.tonshoister.models.Workout;
import com.hoister.tonshoister.services.WorkoutService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {
  private final WorkoutService workoutService;
  private final DTOsMapper DTOsMapper;

  public WorkoutController(WorkoutService workoutService, DTOsMapper DTOsMapper) {
    this.workoutService = workoutService;
    this.DTOsMapper = DTOsMapper;
  }

  @PostMapping("/{programId}")
  public ResponseEntity<WorkoutDTO> createProgram(@Valid @RequestBody WorkoutDTO workoutDTO,
      @PathVariable Integer programId) {
    Workout createdWorkout = workoutService
        .createWorkout(DTOsMapper.convertToEntity(workoutDTO), programId);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(DTOsMapper.convertToDto(createdWorkout));
  }
}