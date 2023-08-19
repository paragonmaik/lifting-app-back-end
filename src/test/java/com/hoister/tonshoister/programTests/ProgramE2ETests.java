package com.hoister.tonshoister.programTests;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.hoister.tonshoister.models.Program;
import com.hoister.tonshoister.repositories.ProgramRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.util.List;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProgramE2ETests {

  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  ProgramRepository programRepository;
  @Autowired
  TestRestTemplate testRestTemplate;

  @BeforeEach
  public void setUp() {
    programRepository.deleteAll();
  }

  @Test
  public void createProgramSuccess() throws Exception {
    Program program = new Program("Starting Strength", 40, "Rookie program.");
    ResponseEntity<Program> response = testRestTemplate.postForEntity("/api/programs", program,
        Program.class);
    Program responseProgram = response.getBody();

    assertEquals(response.getStatusCode(), HttpStatus.CREATED);

    assertNotNull(responseProgram.getId());
    assertEquals(responseProgram.getName(), program.getName());
    assertEquals(responseProgram.getDurationWeeks(), program.getDurationWeeks());
    assertEquals(responseProgram.getDescription(), program.getDescription());
    assertNotNull(responseProgram.getDateCreated());
    assertNotNull(responseProgram.getWorkouts());
  }

  @Test
  public void getAllProgramsSuccess() throws Exception {
    Program program = new Program("GVT", 12, "German Volume training");
    programRepository.save(program);

    ResponseEntity<List<Program>> response = testRestTemplate
        .exchange("/api/programs", HttpMethod.GET, null, new ParameterizedTypeReference<List<Program>>() {
        });
    Program responseProgram = response.getBody().get(0);

    assertEquals(response.getStatusCode(), HttpStatus.OK);

    assertNotNull(responseProgram.getId());
    assertEquals(responseProgram.getName(), program.getName());
    assertEquals(responseProgram.getDurationWeeks(), program.getDurationWeeks());
    assertEquals(responseProgram.getDescription(), program.getDescription());
    assertNotNull(responseProgram.getDateCreated());
    assertNotNull(responseProgram.getWorkouts());
  }

  @Test
  public void getAllProgramsThrowsException() throws Exception {
    ResponseEntity<Program> responseProgram = testRestTemplate
        .getForEntity("/api/programs", Program.class);

    assertEquals(responseProgram.getStatusCode(), HttpStatus.NOT_FOUND);
  }

  @Test
  public void getProgramByIdSuccess() throws Exception {
    Program program = new Program("GVT", 12, "German Volume training");
    programRepository.save(program);

    ResponseEntity<Program> response = testRestTemplate
        .getForEntity("/api/programs/1", Program.class);
    Program responseProgram = response.getBody();

    assertEquals(response.getStatusCode(), HttpStatus.OK);

    assertNotNull(responseProgram.getId());
    assertEquals(responseProgram.getName(), program.getName());
    assertEquals(responseProgram.getDurationWeeks(), program.getDurationWeeks());
    assertEquals(responseProgram.getDescription(), program.getDescription());
    assertNotNull(responseProgram.getDateCreated());
    assertNotNull(responseProgram.getWorkouts());
  }

  @Test
  public void getProgramByIdThrowsException() throws Exception {

    ResponseEntity<Program> responseProgram = testRestTemplate
        .getForEntity("/api/programs", Program.class);

    assertEquals(responseProgram.getStatusCode(), HttpStatus.NOT_FOUND);
  }

  @Test
  public void updateProgramSuccess() throws Exception {
    Program program1 = new Program("Starting Strength", 40, "Rookie program.");
    Program program2 = new Program(1, "5x5", 10, "Rookie program.", null, null);
    String requestBody = objectMapper.writeValueAsString(program2);
    HttpHeaders headers = new HttpHeaders();

    programRepository.save(program1);
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<String>(requestBody, headers);

    ResponseEntity<Program> response = testRestTemplate.exchange("/api/programs",
        HttpMethod.PUT, entity,
        Program.class);

    assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
  }

  @Test
  public void updateProgramThrowsException() throws Exception {
    Program program = new Program(1, "5x5", 10, "Rookie program.", null, null);
    String requestBody = objectMapper.writeValueAsString(program);
    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<String>(requestBody, headers);

    ResponseEntity<Program> response = testRestTemplate.exchange("/api/programs",
        HttpMethod.PUT, entity,
        Program.class);

    assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
  }

  @Test
  public void deleteProgramSuccess() throws Exception {
    Program program = new Program("Starting Strength", 40, "Rookie program.");
    programRepository.save(program);

    ResponseEntity<Program> response = testRestTemplate.exchange("/api/programs/1",
        HttpMethod.DELETE, null, Program.class);

    assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
  }

  @Test
  public void deleteProgramThrowsException() throws Exception {
    ResponseEntity<Program> response = testRestTemplate.exchange("/api/programs/1",
        HttpMethod.DELETE, null, Program.class);

    assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
  }
}
