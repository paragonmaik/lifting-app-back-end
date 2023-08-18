package com.hoister.tonshoister.programTests;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoister.tonshoister.models.Program;
import com.hoister.tonshoister.models.Workout;
import com.hoister.tonshoister.repositories.ProgramRepository;
import com.hoister.tonshoister.repositories.WorkoutRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class WorkoutE2ETests {

  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  WorkoutRepository workoutRepository;
  @Autowired
  ProgramRepository programRepository;
  @Autowired
  TestRestTemplate testRestTemplate;

  @BeforeEach
  public void setUp() {
    Program program = new Program(1, "5x5", 52, "A really long program.");

    workoutRepository.deleteAll();
    programRepository.save(program);
  }

  @Test
  public void createWorkoutSuccess() {
    Workout workout = new Workout("Workout A", 12, "Strength workout.");
    ResponseEntity<Workout> response = testRestTemplate.postForEntity("/api/workouts/1", workout,
        Workout.class);
    Workout responseWorkout = response.getBody();

    assertEquals(response.getStatusCode(), HttpStatus.CREATED);

    assertNotNull(responseWorkout.getId());
    assertEquals(responseWorkout.getName(), workout.getName());
    assertEquals(responseWorkout.getDurationMins(), workout.getDurationMins());
    assertEquals(responseWorkout.getDescription(), workout.getDescription());
    assertNotNull(responseWorkout.getDateCreated());
  }
}
