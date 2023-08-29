package com.hoister.tonshoister.authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoister.tonshoister.DTOs.RegisterDTO;
import com.hoister.tonshoister.models.User;
import com.hoister.tonshoister.models.UserRole;
import com.hoister.tonshoister.repositories.UserRepository;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthenticationE2ETests {

  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  TestRestTemplate testRestTemplate;
  @Autowired
  UserRepository userRepository;

  @BeforeEach
  public void setUp() {
    User user = new User(
        "ronnie", "lightweightbabeeee", UserRole.ADMIN);

    userRepository.save(user);
  }

  @Test
  public void registerUserSuccess() throws Exception {
    RegisterDTO registerDTO = new RegisterDTO(
        "arnold", "gettothechoppah", UserRole.ADMIN);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<String>(
        objectMapper.writeValueAsString(registerDTO), headers);

    ResponseEntity<RegisterDTO> response = testRestTemplate.exchange("/api/auth/register",
        HttpMethod.POST, entity,
        RegisterDTO.class);

    assertEquals(response.getStatusCode(), HttpStatus.OK);
  }

  @Test
  public void registerUserThrowsException() throws Exception {
    RegisterDTO registerDTO = new RegisterDTO(
        "ronnie", "lightweightbabeeee", UserRole.ADMIN);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<String>(
        objectMapper.writeValueAsString(registerDTO), headers);

    ResponseEntity<RegisterDTO> response = testRestTemplate.exchange("/api/auth/register",
        HttpMethod.POST, entity,
        RegisterDTO.class);

    assertEquals(response.getStatusCode(), HttpStatus.CONFLICT);
  }
}
