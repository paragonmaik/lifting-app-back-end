package com.hoister.tonshoister.workout;

import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoister.tonshoister.DTOs.DTOsMapper;
import com.hoister.tonshoister.DTOs.WorkoutDTO;
import com.hoister.tonshoister.advisors.UserIdDoesNotMatchException;
import com.hoister.tonshoister.advisors.WorkoutNotFoundException;
import com.hoister.tonshoister.controllers.WorkoutController;
import com.hoister.tonshoister.models.Workout;
import com.hoister.tonshoister.repositories.UserRepository;
import com.hoister.tonshoister.security.TokenService;
import com.hoister.tonshoister.services.PrincipalService;
import com.hoister.tonshoister.services.WorkoutService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(WorkoutController.class)
public class WorkoutControllerTests {

  @Autowired
  ObjectMapper objectMapper;
  @MockBean
  PrincipalService principalService;
  @MockBean
  TokenService tokenService;
  @MockBean
  UserRepository userRepository;
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
        workout.getId(), workout.getUserId(), workout.getName(), workout.getDurationMins(),
        workout.getDescription(), LocalDateTime.now(), null);

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
        workout.getId(), workout.getUserId(), workout.getName(), workout.getDurationMins(),
        workout.getDescription(), LocalDateTime.now(), null);

    List<Workout> workouts = new ArrayList<Workout>();
    List<WorkoutDTO> workoutsDTO = new ArrayList<WorkoutDTO>();
    workouts.add(workout);
    workoutsDTO.add(workoutDTO);

    when(workoutService.findAllByUserId()).thenReturn(workouts);
    when(DTOsMapper.convertToDto(any(Workout.class))).thenReturn(workoutDTO);

    mockMvc.perform(get("/api/workouts"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$..id").value(workoutsDTO.get(0).id()))
        .andExpect(MockMvcResultMatchers.jsonPath("$..name").value(workoutsDTO.get(0).name()))
        .andExpect(MockMvcResultMatchers.jsonPath("$..durationMins").value(workoutsDTO.get(0).durationMins()))
        .andExpect(MockMvcResultMatchers.jsonPath("$..description").value(workoutsDTO.get(0).description()))
        .andExpect(MockMvcResultMatchers.jsonPath("$..dateCreated").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$..exercises").exists());

  }

  @Test
  public void getWorkoutsThrowsException() throws Exception {
    when(workoutService.findAllByUserId()).thenThrow(new WorkoutNotFoundException());

     mockMvc.perform(get("/api/workouts"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$..message").exists());

        verify(workoutService).findAllByUserId();    
  }

  @Test
  public void updateWorkoutSuccess() throws Exception {
    Workout workout = new Workout(1, "Workout A", 12, "A long workout.");

    when(DTOsMapper.convertToEntity(any(WorkoutDTO.class))).thenReturn(workout);
    when(workoutService.updateWorkout(any(Workout.class))).thenReturn(workout);

    mockMvc
        .perform(
            put("/api/workouts").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(workout)))
        .andExpect(MockMvcResultMatchers.status().isNoContent());

    verify(DTOsMapper).convertToEntity(any(WorkoutDTO.class));
    verify(workoutService).updateWorkout(any(Workout.class));
  }

  @Test
  public void updateWorkoutThrowsException() throws Exception {
    Workout workout = new Workout(1, "Workout A", 12, "A long workout.");

    when(DTOsMapper.convertToEntity(any(WorkoutDTO.class))).thenReturn(workout);
    when(workoutService.updateWorkout(any(Workout.class)))
        .thenThrow(new WorkoutNotFoundException());

    mockMvc.perform(put("/api/workouts").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(workout)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").exists());

    verify(DTOsMapper).convertToEntity(any(WorkoutDTO.class));
    verify(workoutService).updateWorkout(any(Workout.class));
  }

  @Test
  public void updateWorkoutThrowsUserIdDoesNotMatchException() throws Exception {
    Workout workout = new Workout(1, "Workout A", 12, "A long workout.");

    when(DTOsMapper.convertToEntity(any(WorkoutDTO.class))).thenReturn(workout);
    when(workoutService.updateWorkout(any(Workout.class)))
        .thenThrow(new UserIdDoesNotMatchException());

    mockMvc.perform(put("/api/workouts").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(workout)))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.message").exists());

    verify(DTOsMapper).convertToEntity(any(WorkoutDTO.class));
    verify(workoutService).updateWorkout(any(Workout.class));
  }

  @Test
  public void deleteWorkoutSuccess() throws Exception {
    doNothing().when(workoutService).deleteWorkout(1);

    mockMvc
        .perform(
            delete("/api/workouts/1"))
        .andExpect(MockMvcResultMatchers.status().isNoContent());

    verify(workoutService).deleteWorkout(1);
  }

  @Test
  public void deleteWorkoutThrowsException() throws Exception {
    doThrow(new WorkoutNotFoundException()).when(workoutService).deleteWorkout(1);

    mockMvc
        .perform(
            delete("/api/workouts/1"))
        .andExpect(MockMvcResultMatchers.status().isNotFound());

    verify(workoutService).deleteWorkout(1);
  }
}
