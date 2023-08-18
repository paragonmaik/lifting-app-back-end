package com.hoister.tonshoister.programTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoister.tonshoister.DTOs.DTOsMapper;
import com.hoister.tonshoister.DTOs.WorkoutDTO;
import com.hoister.tonshoister.advisors.WorkoutNotFoundException;
import com.hoister.tonshoister.controllers.WorkoutController;
import com.hoister.tonshoister.models.Program;
import com.hoister.tonshoister.models.Workout;
import com.hoister.tonshoister.services.ProgramService;
import com.hoister.tonshoister.services.WorkoutService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkoutController.class)
public class WorkoutControllerTests {

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  WorkoutService workoutService;
  @MockBean
  DTOsMapper DTOsMapper;
  @Autowired
  MockMvc mockMvc;

  @Test
  public void createWorkoutSuccess() throws Exception {
    Workout workout = new Workout(1, "Workout A", 12, "A long workout.");
    WorkoutDTO workoutDTO = new WorkoutDTO(
        workout.getId(), workout.getName(), workout.getDurationMins(),
        workout.getDescription(), LocalDateTime.now());

    when(DTOsMapper.convertToEntity(any(WorkoutDTO.class))).thenReturn(workout);
    when(workoutService.createWorkout(workout, 1)).thenReturn(workout);
    when(DTOsMapper.convertToDto(any(Workout.class))).thenReturn(workoutDTO);

    mockMvc
        .perform(post("/api/workouts/1").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(workout)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(workoutDTO.id())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(workoutDTO.name())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.durationMins", CoreMatchers.is(workoutDTO.durationMins())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is(workoutDTO.description())));

    verify(DTOsMapper).convertToEntity(any(WorkoutDTO.class));
    verify(workoutService).createWorkout(workout, 1);
    verify(DTOsMapper).convertToDto(any(Workout.class));
  }

  @Test
  public void getWorkoutsSuccess() throws Exception {
    Workout workout = new Workout(1, "Workout A", 12, "A long workout.");
    WorkoutDTO workoutDTO = new WorkoutDTO(
        workout.getId(), workout.getName(), workout.getDurationMins(),
        workout.getDescription(), LocalDateTime.now());

    List<Workout> workouts = new ArrayList<Workout>();
    List<WorkoutDTO> workoutsDTO = new ArrayList<WorkoutDTO>();
    workouts.add(workout);
    workoutsDTO.add(workoutDTO);

    when(workoutService.findAll()).thenReturn(workouts);
    when(DTOsMapper.convertToDto(any(Workout.class))).thenReturn(workoutDTO);

    mockMvc.perform(get("/api/workouts"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$..id").value(workoutsDTO.get(0).id()))
        .andExpect(MockMvcResultMatchers.jsonPath("$..name").value(workoutsDTO.get(0).name()))
        .andExpect(MockMvcResultMatchers.jsonPath("$..durationMins").value(workoutsDTO.get(0).durationMins()))
        .andExpect(MockMvcResultMatchers.jsonPath("$..description").value(workoutsDTO.get(0).description()))
        .andExpect(MockMvcResultMatchers.jsonPath("$..dateCreated").exists());

  }

  @Test
  public void getWorkoutsThrowsException() throws Exception {
    when(workoutService.findAll()).thenThrow(new WorkoutNotFoundException());

     mockMvc.perform(get("/api/workouts"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$..message").exists());

        verify(workoutService).findAll();    
 
  }
}
