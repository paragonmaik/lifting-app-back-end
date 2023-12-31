package com.hoister.tonshoister.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoister.tonshoister.DTOs.ExerciseDTO;
import com.hoister.tonshoister.advisors.ExerciseNotFoundException;
import com.hoister.tonshoister.repositories.ExerciseRepository;

@Service
public class ExecutionOrderService {

  @Autowired
  PrincipalService principalService;
  @Autowired
  private ExerciseRepository exerciseRepository;

  public void updateExercisesExecOrder(List<ExerciseDTO> exerciseDTOs) throws ExerciseNotFoundException {
    exerciseDTOs.stream()
        .forEach(exerciseDTO -> {
          var foundExercise = exerciseRepository
              .findById(exerciseDTO.id())
              .orElseThrow(() -> new ExerciseNotFoundException());

          foundExercise.setExecOrder(exerciseDTO.execOrder());
          exerciseRepository.save(foundExercise);
        });
  }
}
