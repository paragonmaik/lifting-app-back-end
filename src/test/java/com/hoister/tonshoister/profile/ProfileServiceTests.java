package com.hoister.tonshoister.profile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hoister.tonshoister.advisors.ProfileNotFoundException;
import com.hoister.tonshoister.advisors.UserIdDoesNotMatchException;
import com.hoister.tonshoister.models.*;
import com.hoister.tonshoister.repositories.ProfileRepository;
import com.hoister.tonshoister.services.PrincipalService;
import com.hoister.tonshoister.services.ProfileService;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTests {
  private String userId = "37755df9-5607-495e-b5d4-da4f01f7c665";

  @Mock
  PrincipalService principalService;
  @Mock
  ProfileRepository profileRepository;
  @InjectMocks
  ProfileService profileService;

  @Test
  public void createProfileSuccess() {
    User user = new User(userId, "arnold", "gettothechoppah", UserRole.ADMIN, null);
    Profile profile = new Profile(user.getId(), null, null, user,
        new HashSet<Program>(), new HashSet<Workout>(), new HashSet<Exercise>());

    when(profileRepository.save(any(Profile.class))).thenReturn(profile);
    profileService.createProfile(user);

    verify(profileRepository).save(any(Profile.class));
  }

  @Test
  public void getProfileSuccess() throws Exception {
    Optional<Profile> profile = Optional.of(
        new Profile(userId, 70, 175, null,
            null, null, null));

    when(principalService.getAuthUserId()).thenReturn(userId);
    when(profileRepository.findById(userId)).thenReturn(profile);

    Optional<Profile> requestedProgram = Optional.of(
        profileService.findById());

    assertEquals(requestedProgram, profile);
    verify(profileRepository).findById(userId);
  }

  @Test
  public void getProfileThrowsException() throws Exception {
    Optional<Profile> profile = Optional.empty();

    when(principalService.getAuthUserId()).thenReturn(userId);
    when(profileRepository.findById(userId)).thenReturn(profile);

    assertThrows(ProfileNotFoundException.class, () -> {
      profileService.findById();
    });

    verify(profileRepository).findById(userId);
  }

  @Test
  public void updateProfileSuccess() throws Exception {
    Profile profile1 = new Profile(userId, 70, 175, null,
        new HashSet<Program>(), new HashSet<Workout>(), new HashSet<Exercise>());
    Profile profile2 = new Profile(userId, 75, 176, null,
        new HashSet<Program>(), new HashSet<Workout>(), new HashSet<Exercise>());

    when(principalService.getAuthUserId()).thenReturn(userId);
    when(profileRepository.findById(userId)).thenReturn(Optional.of(profile1));
    when(profileRepository.save(profile1)).thenReturn(profile2);

    Profile updatedProfile = profileService.updateProfile(profile1);

    assertNotEquals(updatedProfile.getWeight(), profile1.getWeight());
    assertNotEquals(updatedProfile.getHeight(), profile1.getHeight());

    verify(profileRepository).findById(userId);
    verify(profileRepository).save(profile1);
  }

  @Test
  public void updateProfileThrowsException() {
    Profile profile1 = new Profile(userId, 70, 175, null,
        new HashSet<Program>(), new HashSet<Workout>(), new HashSet<Exercise>());

    when(principalService.getAuthUserId()).thenReturn(userId);
    when(profileRepository.findById(userId)).thenReturn(Optional.empty());

    assertThrows(UserIdDoesNotMatchException.class, () -> {
      profileService.updateProfile(profile1);
    });

    verify(profileRepository).findById(userId);
  }
}
