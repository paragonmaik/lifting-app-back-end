package com.hoister.tonshoister.exercise;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoister.tonshoister.DTOs.AuthenticationDTO;
import com.hoister.tonshoister.DTOs.LoginResponseDTO;
import com.hoister.tonshoister.models.Exercise;
import com.hoister.tonshoister.models.GoalType;
import com.hoister.tonshoister.models.User;
import com.hoister.tonshoister.models.UserRole;
import com.hoister.tonshoister.models.Workout;
import com.hoister.tonshoister.repositories.ExerciseRepository;
import com.hoister.tonshoister.repositories.UserRepository;
import com.hoister.tonshoister.repositories.WorkoutRepository;
import com.hoister.tonshoister.security.TokenService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestInstance(Lifecycle.PER_CLASS)
public class ExerciseE2ETests {
  private String token;
  private String userId;

  @Autowired
  UserRepository userRepository;
  @Autowired
  TokenService tokenService;
  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  ExerciseRepository exerciseRepository;
  @Autowired
  WorkoutRepository workoutRepository;
  @Autowired
  TestRestTemplate testRestTemplate;

  @BeforeAll
  public void setUser() {
    User user = new User("arnold", "gettothechoppa", UserRole.USER);
    testRestTemplate.postForEntity("/api/auth/register", user,
        Void.class);

    AuthenticationDTO auth = new AuthenticationDTO(user.getLogin(), user.getPassword());

    ResponseEntity<LoginResponseDTO> loginResponse = testRestTemplate.postForEntity(
        "/api/auth/login", auth,
        LoginResponseDTO.class);

    token = "Bearer " + loginResponse.getBody().token();

    String login = tokenService.validateToken(token.replace("Bearer ", ""));
    User foundUser = (User) userRepository.findByLogin(login);
    userId = foundUser.getId();
  }

  @BeforeEach
  public void setUp() {
    Workout workout = new Workout(
        null, null, "Workout A", 70, "A long workout.", null, null, null);
    Exercise exercise = new Exercise(
        1, null, "High Bar Squat", 120, GoalType.STRENGTH,
        150, "No instructions.", null, null);

    exerciseRepository.deleteAll();

    exerciseRepository.save(exercise);
    workoutRepository.save(workout);
  }

  @Test
  public void createExerciseSuccess() throws Exception {
    Exercise exercise = new Exercise(
        2, "uuid", "Deadlift", 150, GoalType.STRENGTH,
        180, "No instructions.", null, null);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);

    HttpEntity<String> entity = new HttpEntity<String>(
        objectMapper.writeValueAsString(exercise), headers);
    ResponseEntity<Exercise> response = testRestTemplate.exchange("/api/exercises/1",
        HttpMethod.POST, entity,
        Exercise.class);

    Exercise responseExercise = response.getBody();

    assertEquals(response.getStatusCode(), HttpStatus.CREATED);

    assertNotNull(responseExercise.getId());
    assertEquals(responseExercise.getName(), exercise.getName());
    assertEquals(responseExercise.getLoad(), exercise.getLoad());
    assertEquals(responseExercise.getGoal(), exercise.getGoal());
    assertEquals(responseExercise.getRestSeconds(), exercise.getRestSeconds());
    assertEquals(responseExercise.getInstructions(), exercise.getInstructions());
    assertNotNull(responseExercise.getDateCreated());
  }

  @Test
  public void getAllExercisesSuccess() throws Exception {
    Exercise exercise = new Exercise(
        2, "uuid", "Deadlift", 150, GoalType.STRENGTH,
        180, "No instructions.", null, null);
    exercise.setUserId(userId);
    exerciseRepository.save(exercise);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);

    HttpEntity<String> entity = new HttpEntity<String>(
        objectMapper.writeValueAsString(exercise), headers);

    ResponseEntity<List<Exercise>> response = testRestTemplate
        .exchange("/api/exercises", HttpMethod.GET, entity,
            new ParameterizedTypeReference<List<Exercise>>() {
            });

    Exercise responseExercise = response.getBody().get(0);

    assertEquals(response.getStatusCode(), HttpStatus.OK);

    assertNotNull(responseExercise.getId());
    assertEquals(responseExercise.getName(), exercise.getName());
    assertEquals(responseExercise.getLoad(), exercise.getLoad());
    assertEquals(responseExercise.getGoal(), exercise.getGoal());
    assertEquals(responseExercise.getRestSeconds(), exercise.getRestSeconds());
    assertEquals(responseExercise.getInstructions(), exercise.getInstructions());
    assertNotNull(responseExercise.getDateCreated());
  }

  @Test
  public void getAllExercisesThrowsException() throws Exception {
    exerciseRepository.deleteAll();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);

    HttpEntity<String> entity = new HttpEntity<String>(null, headers);

    ResponseEntity<Exercise> responseExercise = testRestTemplate
        .exchange("/api/exercises", HttpMethod.GET, entity, Exercise.class);

    assertEquals(responseExercise.getStatusCode(), HttpStatus.NOT_FOUND);
  }

  @Test
  public void updateExerciseSuccess() throws Exception {
    Exercise exercise1 = new Exercise(
        null, null, "Deadlift", 150, GoalType.STRENGTH,
        180, "No instructions.", null, null);
    exercise1.setUserId(userId);
    Integer exerciseId = exerciseRepository.save(exercise1).getId();
    Exercise exercise2 = new Exercise(
        exerciseId, null, "Romanian Deadlift", 120, GoalType.STRENGTH,
        150, "No instructions.", null, null);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);

    HttpEntity<String> entity = new HttpEntity<String>(
        objectMapper.writeValueAsString(exercise2), headers);
    ResponseEntity<Exercise> response = testRestTemplate.exchange("/api/exercises",
        HttpMethod.PUT, entity,
        Exercise.class);

    assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
  }

  @Test
  public void updateExerciseThrowsException() throws Exception {
    Exercise exercise = new Exercise(
        2, null, "Deadlift", 150, GoalType.STRENGTH, 180, "No instructions.", null, null);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);

    HttpEntity<String> entity = new HttpEntity<String>(
        objectMapper.writeValueAsString(exercise), headers);
    ResponseEntity<Exercise> response = testRestTemplate.exchange("/api/exercises",
        HttpMethod.PUT, entity,
        Exercise.class);

    assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
  }

  @Test
  public void updateExerciseThrowsUserIdDoesNotMatchException() throws Exception {
    Exercise exercise1 = new Exercise(
        null, null, "Deadlift", 150, GoalType.STRENGTH, 180, "No instructions.", null, null);
    Integer exerciseId = exerciseRepository.save(exercise1).getId();
    Exercise exercise2 = new Exercise(
        exerciseId, null, "Romanian Deadlift", 120,
        GoalType.STRENGTH, 150, "No instructions.", null, null);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);

    HttpEntity<String> entity = new HttpEntity<String>(
        objectMapper.writeValueAsString(exercise2), headers);
    ResponseEntity<Exercise> response = testRestTemplate.exchange("/api/exercises",
        HttpMethod.PUT, entity,
        Exercise.class);

    assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
  }

  @Test
  public void deleteExerciseSuccess() throws Exception {
    Exercise exercise = new Exercise(
        null, null, "Deadlift", 150, GoalType.STRENGTH, 180, "No instructions.", null, null);

    exercise.setUserId(userId);
    Integer exerciseId = exerciseRepository
        .save(exercise)
        .getId();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);

    ResponseEntity<Exercise> response = testRestTemplate.exchange("/api/exercises/" + exerciseId,
        HttpMethod.DELETE, new HttpEntity<String>(null, headers), Exercise.class);

    assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
  }

  @Test
  public void deleteExerciseThrowsException() throws Exception {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);

    ResponseEntity<Exercise> response = testRestTemplate.exchange("/api/exercises/2",
        HttpMethod.DELETE, new HttpEntity<String>(null, headers), Exercise.class);

    assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
  }

  @Test
  public void deleteExerciseThrowsUserIdsDoNotMatchException() throws Exception {
    Exercise exercise = new Exercise(
        null, null, "Deadlift", 150, GoalType.STRENGTH, 180, "No instructions.", null, null);

    Integer exerciseId = exerciseRepository
        .save(exercise)
        .getId();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);

    ResponseEntity<Exercise> response = testRestTemplate.exchange("/api/exercises/" + exerciseId,
        HttpMethod.DELETE, new HttpEntity<String>(null, headers), Exercise.class);

    assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
  }
}
