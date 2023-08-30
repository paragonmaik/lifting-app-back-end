package com.hoister.tonshoister.profile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
