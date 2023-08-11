package com.hoister.tonshoister.programTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.hoister.tonshoister.models.Program;
import com.hoister.tonshoister.repositories.ProgramRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.util.List;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProgramE2ETests {

  @Autowired
  ProgramRepository programRepository;
  @Autowired
  TestRestTemplate testRestTemplate;

  @BeforeEach
  public void setUp() {
    programRepository.deleteAll();
  }

  @Test
  public void getAllProgramsSuccess() throws Exception {
    Program program = new Program("GVT", 12, "German Volume training");
    programRepository.save(program);

    ResponseEntity<List<Program>> responseProgram = testRestTemplate
        .exchange("/api/programs", HttpMethod.GET, null, new ParameterizedTypeReference<List<Program>>() {
        });

    assertEquals(responseProgram.getStatusCode(), HttpStatus.OK);
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

    ResponseEntity<Program> responseProgram = testRestTemplate
        .getForEntity("/api/programs/1", Program.class);

    assertEquals(responseProgram.getStatusCode(), HttpStatus.OK);
    assertNotNull(responseProgram);
  }

  @Test
  public void getProgramByIdThrowsException() throws Exception {

    ResponseEntity<Program> responseProgram = testRestTemplate
        .getForEntity("/api/programs", Program.class);

    assertEquals(responseProgram.getStatusCode(), HttpStatus.NOT_FOUND);
  }
}
