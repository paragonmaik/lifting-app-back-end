package com.hoister.tonshoister.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoister.tonshoister.advisors.UserIdDoesNotMatchException;
import com.hoister.tonshoister.models.Profile;
import com.hoister.tonshoister.models.User;
import com.hoister.tonshoister.repositories.ProfileRepository;

@Service
public class ProfileService {

  @Autowired
  PrincipalService principalService;
  @Autowired
  ProfileRepository profileRepository;

  public void createProfile(User user) {
    Profile profile = new Profile();
    profile.setUser(user);

    profileRepository.save(profile);
  }

  public Profile updateProfile(Profile profile) throws Exception {
    var userId = principalService.getAuthUserId();

    Profile foundProfile = profileRepository.findById(userId)
        .orElseThrow(() -> new UserIdDoesNotMatchException());

    foundProfile.setWeight(profile.getWeight());
    foundProfile.setHeight(profile.getHeight());

    return profileRepository.save(foundProfile);
  }
}
