package com.hoister.tonshoister.authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoister.tonshoister.DTOs.AuthenticationDTO;
import com.hoister.tonshoister.DTOs.LoginResponseDTO;
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

    testRestTemplate.postForEntity("/api/auth/register", user,
        Void.class);
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

  @Test
  public void loginUserSuccess() throws Exception {
    AuthenticationDTO authDTO = new AuthenticationDTO(
        "ronnie", "lightweightbabeeee");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<String>(
        objectMapper.writeValueAsString(authDTO), headers);

    ResponseEntity<LoginResponseDTO> response = testRestTemplate.exchange("/api/auth/login",
        HttpMethod.POST, entity,
        LoginResponseDTO.class);

    assertEquals(response.getStatusCode(), HttpStatus.OK);
  }

  @Test
  public void loginUserThrowsException() throws Exception {
    AuthenticationDTO authDTO = new AuthenticationDTO(
        "ronnie", "everybodywannabeabodybuilder");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<String>(
        objectMapper.writeValueAsString(authDTO), headers);

    ResponseEntity<LoginResponseDTO> response = testRestTemplate.exchange("/api/auth/login",
        HttpMethod.POST, entity,
        LoginResponseDTO.class);

    assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);

  }
}
