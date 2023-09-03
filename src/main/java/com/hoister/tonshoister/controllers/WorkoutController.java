package com.hoister.tonshoister.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.hoister.tonshoister.DTOs.DTOsMapper;
import com.hoister.tonshoister.DTOs.WorkoutDTO;
import com.hoister.tonshoister.models.Workout;
import com.hoister.tonshoister.services.WorkoutService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

  @Autowired
  private WorkoutService workoutService;
  @Autowired
  private DTOsMapper DTOsMapper;

  @PostMapping("/{programId}")
  public ResponseEntity<WorkoutDTO> createWorkout(@Valid @RequestBody WorkoutDTO workoutDTO,
      @PathVariable Integer programId) {
    Workout createdWorkout = workoutService
        .createWorkout(DTOsMapper.convertToEntity(workoutDTO), programId);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(DTOsMapper.convertToDto(createdWorkout));
  }

  @GetMapping
  public List<WorkoutDTO> getWorkouts() {
    return workoutService.findAllByUserId()
        .stream().map(workout -> DTOsMapper.convertToDto(workout)).toList();
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PutMapping
  public void updateWorkout(@Valid @RequestBody WorkoutDTO workoutDTO) {
    workoutService.updateWorkout(DTOsMapper.convertToEntity(workoutDTO));
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{id}")
  public void deleteWorkout(@PathVariable Integer id) {
    workoutService.deleteWorkout(id);
  }
}
