package com.hoister.tonshoister.authentication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoister.tonshoister.DTOs.AuthenticationDTO;
import com.hoister.tonshoister.DTOs.DTOsMapper;
import com.hoister.tonshoister.DTOs.RegisterDTO;
import com.hoister.tonshoister.advisors.UserAlreadyRegisteredException;
import com.hoister.tonshoister.controllers.AuthenticationController;
import com.hoister.tonshoister.models.User;
import com.hoister.tonshoister.models.UserRole;
import com.hoister.tonshoister.repositories.UserRepository;
import com.hoister.tonshoister.security.TokenService;
import com.hoister.tonshoister.services.AuthenticationService;
import com.hoister.tonshoister.services.PrincipalService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTests {

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  PrincipalService principalService;
  @MockBean
  UserRepository userRepository;
  @MockBean
  TokenService tokenService;
  @MockBean
  AuthenticationService authenticationService;
  @MockBean
  DTOsMapper DTOsMapper;
  @Autowired
  MockMvc mockMvc;

  @Test
  public void registerUserSuccess() throws Exception {
    User user = new User(
        "arnold", "gettothechoppah", UserRole.ADMIN);

    when(DTOsMapper.convertToEntity(any(RegisterDTO.class))).thenReturn(user);
    doNothing().when(authenticationService).registerUser(user);

    mockMvc
        .perform(
            post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
        .andExpect(MockMvcResultMatchers.status().isOk());

    verify(authenticationService).registerUser(user);
  }

  @Test
  public void registerUserThrowsException() throws Exception {
    User user = new User(
        "arnold", "gettothechoppah", UserRole.ADMIN);

    when(DTOsMapper.convertToEntity(any(RegisterDTO.class))).thenReturn(user);
    doThrow(UserAlreadyRegisteredException.class).when(authenticationService).registerUser(user);

    mockMvc
        .perform(
            post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
        .andExpect(MockMvcResultMatchers.status().isConflict());

    verify(authenticationService).registerUser(user);
  }

  @Test
  public void loginUserSuccess() throws Exception {
    AuthenticationDTO authDTO = new AuthenticationDTO(
        "arnold", "gettothechoppah");

    when(authenticationService.loginUser(authDTO)).thenReturn("");

    mockMvc
        .perform(
            post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authDTO)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists())
        .andExpect(MockMvcResultMatchers.status().isOk());

    verify(authenticationService).loginUser(authDTO);
  }
}
