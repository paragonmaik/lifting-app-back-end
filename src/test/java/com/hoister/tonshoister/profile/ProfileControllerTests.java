package com.hoister.tonshoister.profile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashSet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoister.tonshoister.DTOs.*;
import com.hoister.tonshoister.advisors.ProfileNotFoundException;
import com.hoister.tonshoister.controllers.ProfileController;
import com.hoister.tonshoister.models.*;
import com.hoister.tonshoister.repositories.UserRepository;
import com.hoister.tonshoister.security.TokenService;
import com.hoister.tonshoister.services.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ProfileController.class)
public class ProfileControllerTests {
  private String userId = "37755df9-5607-495e-b5d4-da4f01f7c665";

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  PrincipalService principalService;
  @MockBean
  UserRepository userRepository;
  @MockBean
  TokenService tokenService;
  @MockBean
  ProfileService profileService;
  @MockBean
  DTOsMapper DTOsMapper;
  @Autowired
  MockMvc mockMvc;

  @Test
  public void updateProfileSuccess() throws Exception {
    Profile profile = new Profile(
        userId, 75, 175, null,
        new HashSet<Program>(), new HashSet<Workout>(), new HashSet<Exercise>());

    when(DTOsMapper.convertToEntity(any(ProfileDTO.class))).thenReturn(profile);
    when(profileService.updateProfile(any(Profile.class))).thenReturn(profile);

    mockMvc
        .perform(
            put("/api/profile/edit").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profile)))
        .andExpect(MockMvcResultMatchers.status().isNoContent());

    verify(DTOsMapper).convertToEntity(any(ProfileDTO.class));
    verify(profileService).updateProfile(any(Profile.class));
  }

  @Test
  public void updateProfileThrowsException() throws Exception {
    Profile profile = new Profile(userId, 75, 175, null,
        new HashSet<Program>(), new HashSet<Workout>(), new HashSet<Exercise>());

    when(DTOsMapper.convertToEntity(any(ProfileDTO.class))).thenReturn(profile);
    when(profileService.updateProfile(any(Profile.class))).thenThrow(new ProfileNotFoundException());

    mockMvc.perform(put("/api/profile/edit").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(profile)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").exists());

    verify(DTOsMapper).convertToEntity(any(ProfileDTO.class));
    verify(profileService).updateProfile(any(Profile.class));
  }
}
