package com.hoister.tonshoister.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.hoister.tonshoister.DTOs.ExerciseDTO;
import com.hoister.tonshoister.models.Exercise;
import com.hoister.tonshoister.services.ExecutionOrderService;

import jakarta.validation.Valid;

@RestController
public class ExecutionOrderController {
  @Autowired
  private ExecutionOrderService executionOrderService;

  @PutMapping("/api/exercises/reorder")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public List<Exercise> updateExercisesExecOrder(@Valid @RequestBody List<ExerciseDTO> exerciseDTOs) {

    return executionOrderService.updateExercisesExecOrder(exerciseDTOs);
  }
}
