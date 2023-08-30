package com.hoister.tonshoister.profile;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hoister.tonshoister.advisors.ProfileNotFoundException;
import com.hoister.tonshoister.models.Profile;
import com.hoister.tonshoister.models.User;
import com.hoister.tonshoister.models.UserRole;
import com.hoister.tonshoister.repositories.ProfileRepository;
import com.hoister.tonshoister.services.ProfileService;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTests {

  @Mock
  ProfileRepository profileRepository;
  @InjectMocks
  ProfileService profileService;

  @Test
  public void createProfileSuccess() {
    User user = new User("uuid", "arnold", "gettothechoppah", UserRole.ADMIN, null);
    Profile profile = new Profile(user.getId(), null, null, user);

    when(profileRepository.save(any(Profile.class))).thenReturn(profile);
    profileService.createProfile(user);

    verify(profileRepository).save(any(Profile.class));
  }

  @Test
  public void updateProfileSuccess() throws Exception {
    Profile profile1 = new Profile("uuid", 70, 175, null);
    Profile profile2 = new Profile("uuid", 75, 176, null);

    when(profileRepository.findById("uuid")).thenReturn(Optional.of(profile1));
    when(profileRepository.save(profile1)).thenReturn(profile2);

    Profile updatedProfile = profileService.updateProfile(profile1);

    assertNotEquals(profile1.getWeight(), updatedProfile.getWeight());
    assertNotEquals(profile1.getHeight(), updatedProfile.getHeight());

    verify(profileRepository).findById("uuid");
    verify(profileRepository).save(profile1);
  }

  @Test
  public void updateProfileThrowsException() {
    Profile profile1 = new Profile("uuid", 70, 175, null);

    when(profileRepository.findById("uuid")).thenReturn(Optional.empty());

    assertThrows(ProfileNotFoundException.class, () -> {
      profileService.updateProfile(profile1);
    });

    verify(profileRepository).findById("uuid");
  }
}
