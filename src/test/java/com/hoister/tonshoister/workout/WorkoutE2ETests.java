package com.hoister.tonshoister.workout;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoister.tonshoister.DTOs.AuthenticationDTO;
import com.hoister.tonshoister.DTOs.LoginResponseDTO;
import com.hoister.tonshoister.models.Program;
import com.hoister.tonshoister.models.User;
import com.hoister.tonshoister.models.UserRole;
import com.hoister.tonshoister.models.Workout;
import com.hoister.tonshoister.repositories.ProgramRepository;
import com.hoister.tonshoister.repositories.WorkoutRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class WorkoutE2ETests {
  private String token;

  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  WorkoutRepository workoutRepository;
  @Autowired
  ProgramRepository programRepository;
  @Autowired
  TestRestTemplate testRestTemplate;

  @BeforeEach
  public void setUser() {
    User user = new User("arnold", "gettothechoppa", UserRole.USER);
    testRestTemplate.postForEntity("/api/auth/register", user,
        Void.class);

    AuthenticationDTO auth = new AuthenticationDTO(user.getLogin(), user.getPassword());

    ResponseEntity<LoginResponseDTO> loginResponse = testRestTemplate.postForEntity(
        "/api/auth/login", auth,
        LoginResponseDTO.class);

    token = "Bearer " + loginResponse.getBody().token();
  }

  @BeforeEach
  public void setUp() {
    Program program = new Program(1, null, "5x5", 52, "A really long program.");
    Workout workout = new Workout("Workout A", 12, "Strength workout.");

    workoutRepository.deleteAll();

    workoutRepository.save(workout);
    programRepository.save(program);
  }

  @Test
  public void createWorkoutSuccess() throws Exception {
    Workout workout = new Workout("Workout B", 12, "Strength workout.");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);

    HttpEntity<String> entity = new HttpEntity<String>(
        objectMapper.writeValueAsString(workout), headers);

    ResponseEntity<Workout> response = testRestTemplate.exchange(
        "/api/workouts/1", HttpMethod.POST, entity,
        Workout.class);
    Workout responseWorkout = response.getBody();

    assertEquals(response.getStatusCode(), HttpStatus.CREATED);

    assertNotNull(responseWorkout.getId());
    assertEquals(responseWorkout.getName(), workout.getName());
    assertEquals(responseWorkout.getDurationMins(), workout.getDurationMins());
    assertEquals(responseWorkout.getDescription(), workout.getDescription());
    assertNotNull(responseWorkout.getDateCreated());
    assertNotNull(responseWorkout.getExercises());

  }

  @Test
  public void getAllWorkoutsSuccess() throws Exception {
    Workout workout = new Workout("Workout B", 22, "Cool workout.");
    workoutRepository.save(workout);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);
    HttpEntity<String> entity = new HttpEntity<String>(null, headers);

    ResponseEntity<List<Workout>> response = testRestTemplate
        .exchange("/api/workouts", HttpMethod.GET, entity,
            new ParameterizedTypeReference<List<Workout>>() {
            });

    Workout responseWorkout = response.getBody().get(1);

    assertEquals(response.getStatusCode(), HttpStatus.OK);

    assertNotNull(responseWorkout.getId());
    assertEquals(responseWorkout.getName(), workout.getName());
    assertEquals(responseWorkout.getDurationMins(), workout.getDurationMins());
    assertEquals(responseWorkout.getDescription(), workout.getDescription());
    assertNotNull(responseWorkout.getDateCreated());
    assertNotNull(responseWorkout.getExercises());
  }

  @Test
  public void getAllWorkoutsThrowsException() throws Exception {
    workoutRepository.deleteAll();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);

    HttpEntity<String> entity = new HttpEntity<String>(null, headers);

    ResponseEntity<Workout> response = testRestTemplate
        .exchange("/api/workouts", HttpMethod.GET, entity,
            Workout.class);

    assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
  }

  @Test
  public void updateWorkoutSuccess() throws Exception {
    Workout workout = new Workout(1, "Workout B", 12, "Cool workout.");
    String requestBody = objectMapper.writeValueAsString(workout);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);

    HttpEntity<String> entity = new HttpEntity<String>(requestBody, headers);
    ResponseEntity<Workout> response = testRestTemplate.exchange("/api/workouts",
        HttpMethod.PUT, entity,
        Workout.class);

    assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
  }

  @Test
  public void updateWorkoutThrowsException() throws Exception {
    Workout workout = new Workout(2, "Workout B", 12, "Cool workout.");
    String requestBody = objectMapper.writeValueAsString(workout);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);

    HttpEntity<String> entity = new HttpEntity<String>(requestBody, headers);

    ResponseEntity<Workout> response = testRestTemplate.exchange("/api/workouts",
        HttpMethod.PUT, entity,
        Workout.class);

    assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
  }

  @Test
  public void deleteWorkoutSuccess() throws Exception {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);

    HttpEntity<String> entity = new HttpEntity<String>(null, headers);

    ResponseEntity<Workout> response = testRestTemplate.exchange("/api/workouts/1",
        HttpMethod.DELETE, entity, Workout.class);

    assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
  }

  @Test
  public void deleteWorkoutThrowsException() throws Exception {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);

    HttpEntity<String> entity = new HttpEntity<String>(null, headers);

    ResponseEntity<Workout> response = testRestTemplate.exchange("/api/workouts/2",
        HttpMethod.DELETE, entity, Workout.class);

    assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);

  }
}
