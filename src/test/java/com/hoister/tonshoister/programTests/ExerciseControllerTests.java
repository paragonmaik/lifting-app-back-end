package com.hoister.tonshoister.programTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoister.tonshoister.DTOs.DTOsMapper;
import com.hoister.tonshoister.DTOs.ExerciseDTO;
import com.hoister.tonshoister.controllers.ExerciseController;
import com.hoister.tonshoister.models.Exercise;
import com.hoister.tonshoister.models.GoalType;
import com.hoister.tonshoister.services.ExerciseService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.CoreMatchers;

@WebMvcTest(ExerciseController.class)
public class ExerciseControllerTests {

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  ExerciseService exerciseService;
  @MockBean
  DTOsMapper DTOsMapper;
  @Autowired
  MockMvc mockMvc;

  @Test
  public void createExerciseSuccess() throws Exception {
    Exercise exercise = new Exercise(
        1, "High Bar Squat", 120, GoalType.STRENGTH, 150, "No instructions.");
    ExerciseDTO exerciseDTO = new ExerciseDTO(
        exercise.getId(), exercise.getName(), exercise.getLoad(),
        exercise.getGoal(), exercise.getRestSeconds(), exercise.getInstructions(),
        exercise.getDateCreated());

    when(DTOsMapper.convertToEntity(any(ExerciseDTO.class))).thenReturn(exercise);
    when(exerciseService.createExercise(exercise, 1)).thenReturn(exercise);
    when(DTOsMapper.convertToDto(any(Exercise.class))).thenReturn(exerciseDTO);

    mockMvc
        .perform(post("/api/exercises/1").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(exercise)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(exerciseDTO.id())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(exerciseDTO.name())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.load", CoreMatchers.is(exerciseDTO.load())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.goal", CoreMatchers.is(exerciseDTO.goal().toString())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.restSeconds", CoreMatchers.is(exerciseDTO.restSeconds())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.instructions", CoreMatchers.is(exerciseDTO.instructions())));

    verify(DTOsMapper).convertToEntity(any(ExerciseDTO.class));
    verify(exerciseService).createExercise(exercise, 1);
    verify(DTOsMapper).convertToDto(any(Exercise.class));
  }
}
