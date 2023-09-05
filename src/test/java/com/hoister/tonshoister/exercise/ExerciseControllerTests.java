package com.hoister.tonshoister.exercise;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoister.tonshoister.DTOs.DTOsMapper;
import com.hoister.tonshoister.DTOs.ExerciseDTO;
import com.hoister.tonshoister.advisors.ExerciseNotFoundException;
import com.hoister.tonshoister.advisors.UserIdDoesNotMatchException;
import com.hoister.tonshoister.controllers.ExerciseController;
import com.hoister.tonshoister.models.Exercise;
import com.hoister.tonshoister.models.GoalType;
import com.hoister.tonshoister.models.Workout;
import com.hoister.tonshoister.repositories.UserRepository;
import com.hoister.tonshoister.security.TokenService;
import com.hoister.tonshoister.services.ExerciseService;
import com.hoister.tonshoister.services.PrincipalService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.HashSet;

import org.hamcrest.CoreMatchers;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ExerciseController.class)
public class ExerciseControllerTests {
  private String userId = "37755df9-5607-495e-b5d4-da4f01f7c665";
  Exercise exercise;
  ExerciseDTO exerciseDTO;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  PrincipalService principalService;
  @MockBean
  UserRepository userRepository;
  @MockBean
  TokenService tokenService;
  @MockBean
  ExerciseService exerciseService;
  @MockBean
  DTOsMapper DTOsMapper;
  @Autowired
  MockMvc mockMvc;

  @BeforeEach
  public void setEntities() {
    exercise = new Exercise(
        1, userId, "High Bar Squat", 120, GoalType.STRENGTH,
        150, "Bar rests at the traps.", null, new HashSet<Workout>());

    exerciseDTO = new ExerciseDTO(
        exercise.getId(), exercise.getUserId(), exercise.getName(), exercise.getLoad(),
        exercise.getGoal(), exercise.getRestSeconds(), exercise.getInstructions(),
        exercise.getDateCreated());
  }

  @Test
  public void createExerciseSuccess() throws Exception {
    when(DTOsMapper.convertToEntity(any(ExerciseDTO.class))).thenReturn(exercise);
    when(exerciseService.createExercise(exercise, 1)).thenReturn(exercise);
    when(DTOsMapper.convertToDto(any(Exercise.class))).thenReturn(exerciseDTO);

    mockMvc
        .perform(post("/api/exercises/1").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(exercise)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(exerciseDTO.id())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name",
            CoreMatchers.is(exerciseDTO.name())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.load",
            CoreMatchers.is(exerciseDTO.load())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.goal",
            CoreMatchers.is(exerciseDTO.goal().toString())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.restSeconds",
            CoreMatchers.is(exerciseDTO.restSeconds())))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.instructions",
                CoreMatchers.is(exerciseDTO.instructions())));

    verify(DTOsMapper).convertToEntity(any(ExerciseDTO.class));
    verify(exerciseService).createExercise(exercise, 1);
    verify(DTOsMapper).convertToDto(any(Exercise.class));
  }

  @Test
  public void getExercisesSuccess() throws Exception {
    List<Exercise> exercises = new ArrayList<Exercise>();
    List<ExerciseDTO> exercisesDTO = new ArrayList<ExerciseDTO>();
    exercises.add(exercise);
    exercisesDTO.add(exerciseDTO);

    when(exerciseService.findAllByUserId()).thenReturn(exercises);
    when(DTOsMapper.convertToDto(any(Exercise.class))).thenReturn(exerciseDTO);

    mockMvc.perform(get("/api/exercises"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$..id").value(exercisesDTO.get(0).id()))
        .andExpect(MockMvcResultMatchers.jsonPath("$..name").value(exercisesDTO.get(0).name()))
        .andExpect(MockMvcResultMatchers.jsonPath("$..load").value(exercisesDTO.get(0).load()))
        .andExpect(MockMvcResultMatchers.jsonPath("$..goal")
            .value(exercisesDTO.get(0).goal().toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$..restSeconds")
            .value(exercisesDTO.get(0).restSeconds()))
        .andExpect(MockMvcResultMatchers.jsonPath("$..instructions")
            .value(exercisesDTO.get(0).instructions()))
        .andExpect(MockMvcResultMatchers.jsonPath("$..dateCreated").exists());
  }

  @Test
  public void getExercisesThrowsException() throws Exception {
    when(exerciseService.findAllByUserId()).thenThrow(new ExerciseNotFoundException());

    mockMvc.perform(get("/api/exercises"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$..message").exists());

    verify(exerciseService).findAllByUserId();    
  }

  @Test
  public void updateExerciseSuccess() throws Exception {
    when(DTOsMapper.convertToEntity(any(ExerciseDTO.class))).thenReturn(exercise);
    when(exerciseService.updateExercise(any(Exercise.class))).thenReturn(exercise);

    mockMvc
        .perform(
            put("/api/exercises").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exercise)))
        .andExpect(MockMvcResultMatchers.status().isNoContent());

    verify(DTOsMapper).convertToEntity(any(ExerciseDTO.class));
    verify(exerciseService).updateExercise(any(Exercise.class));
  }

  @Test
  public void updateExerciseThrowsException() throws Exception {
    when(DTOsMapper.convertToEntity(any(ExerciseDTO.class))).thenReturn(exercise);
    when(exerciseService.updateExercise(any(Exercise.class)))
        .thenThrow(new ExerciseNotFoundException());

    mockMvc
        .perform(
            put("/api/exercises").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exercise)))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(jsonPath("$.message").exists());
  }

  @Test
  public void updateExerciseThrowsUserIdDoesNotMatchException() throws Exception {
    when(DTOsMapper.convertToEntity(any(ExerciseDTO.class))).thenReturn(exercise);
    when(exerciseService.updateExercise(any(Exercise.class)))
        .thenThrow(new UserIdDoesNotMatchException());

    mockMvc
        .perform(
            put("/api/exercises").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exercise)))
        .andExpect(MockMvcResultMatchers.status().isForbidden())
        .andExpect(jsonPath("$.message").exists());
  }

  @Test
  public void deleteWorkoutSuccess() throws Exception {
    doNothing().when(exerciseService).deleteExercise(1);

    mockMvc
        .perform(
            delete("/api/exercises/1"))
        .andExpect(MockMvcResultMatchers.status().isNoContent());

    verify(exerciseService).deleteExercise(1);
  }

  @Test
  public void deleteWorkoutThrowsException() throws Exception {
    doThrow(new ExerciseNotFoundException()).when(exerciseService).deleteExercise(1);

    mockMvc
        .perform(
            delete("/api/exercises/1"))
        .andExpect(MockMvcResultMatchers.status().isNotFound());

    verify(exerciseService).deleteExercise(1);
  }

  @Test
  public void deleteExerciseThrowsUserIdsDoNotMatchException() throws Exception {
    doThrow(new UserIdDoesNotMatchException()).when(exerciseService).deleteExercise(1);

    mockMvc
        .perform(
            delete("/api/exercises/1"))
        .andExpect(MockMvcResultMatchers.status().isForbidden());

    verify(exerciseService).deleteExercise(1);
  }
}
