package com.hoister.tonshoister.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoister.tonshoister.DTOs.*;
import com.hoister.tonshoister.models.*;
import com.hoister.tonshoister.repositories.*;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProfileE2ETests {
  private String token;
  private String userId;

  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  ProfileRepository profileRepository;
  @Autowired
  UserRepository userRepository;
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

    User createdUser = (User) userRepository.findByLogin(user.getLogin());

    token = "Bearer " + loginResponse.getBody().token();
    userId = createdUser.getId();
  }

  @Test
  public void getProfileByIdSuccess() throws Exception {
    Profile profile = new Profile(userId, 80, 190, null, null, null, null);
    profileRepository.save(profile);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);
    HttpEntity<String> entity = new HttpEntity<String>(null, headers);

    ResponseEntity<ProfileDTO> response = testRestTemplate
        .exchange("/api/profile", HttpMethod.GET, entity,
            ProfileDTO.class);

    ProfileDTO responseProfile = response.getBody();

    assertEquals(response.getStatusCode(), HttpStatus.OK);

    assertNotNull(responseProfile.id());
    assertEquals(responseProfile.weight(), profile.getWeight());
    assertEquals(responseProfile.height(), profile.getHeight());
  }

  @Test
  public void updateProfileSuccess() throws Exception {
    ProfileDTO profileDTO = new ProfileDTO(userId, 70, 178);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);

    HttpEntity<String> entity = new HttpEntity<String>(
        objectMapper.writeValueAsString(profileDTO), headers);

    ResponseEntity<Profile> response = testRestTemplate.exchange("/api/profile/edit",
        HttpMethod.PUT, entity,
        Profile.class);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }

  @Test
  public void updateProfileThrowsException() throws Exception {
    ProfileDTO profileDTO = new ProfileDTO("nonexistantuuid", 73, 172);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth("expiredtoken");

    HttpEntity<String> entity = new HttpEntity<String>(
        objectMapper.writeValueAsString(profileDTO), headers);

    ResponseEntity<Profile> response = testRestTemplate.exchange("/api/profile/edit",
        HttpMethod.PUT, entity,
        Profile.class);

    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
  }
}
