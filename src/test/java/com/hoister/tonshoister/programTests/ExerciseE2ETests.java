package com.hoister.tonshoister.programTests;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoister.tonshoister.models.Exercise;
import com.hoister.tonshoister.models.GoalType;
import com.hoister.tonshoister.models.Workout;
import com.hoister.tonshoister.repositories.ExerciseRepository;
import com.hoister.tonshoister.repositories.WorkoutRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ExerciseE2ETests {

  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  ExerciseRepository exerciseRepository;
  @Autowired
  WorkoutRepository workoutRepository;
  @Autowired
  TestRestTemplate testRestTemplate;

  @BeforeEach
  public void setUp() {
    Workout workout = new Workout(1, "Workout A", 70, "A long workout.");
    Exercise exercise = new Exercise(
        1, "High Bar Squat", 120, GoalType.STRENGTH, 150, "No instructions.");

    exerciseRepository.deleteAll();

    exerciseRepository.save(exercise);
    workoutRepository.save(workout);
  }

  @Test
  public void createExerciseSuccess() {
    Exercise exercise = new Exercise(
        2, "Deadlift", 150, GoalType.STRENGTH, 180, "No instructions.");

    ResponseEntity<Exercise> response = testRestTemplate.postForEntity(
        "/api/exercises/1", exercise,
        Exercise.class);
    Exercise responseWorkout = response.getBody();

    assertEquals(response.getStatusCode(), HttpStatus.CREATED);

    assertNotNull(responseWorkout.getId());
    assertEquals(responseWorkout.getName(), exercise.getName());
    assertEquals(responseWorkout.getLoad(), exercise.getLoad());
    assertEquals(responseWorkout.getGoal(), exercise.getGoal());
    assertEquals(responseWorkout.getRestSeconds(), exercise.getRestSeconds());
    assertEquals(responseWorkout.getInstructions(), exercise.getInstructions());
    assertNotNull(responseWorkout.getDateCreated());
  }
}
