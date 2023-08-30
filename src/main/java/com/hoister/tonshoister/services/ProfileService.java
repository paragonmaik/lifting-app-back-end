package com.hoister.tonshoister.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoister.tonshoister.models.Profile;
import com.hoister.tonshoister.models.User;
import com.hoister.tonshoister.repositories.ProfileRepository;

@Service
public class ProfileService {

  @Autowired
  ProfileRepository profileRepository;

  public void createProfile(User user) {
    Profile profile = new Profile();
    profile.setUser(user);

    profileRepository.save(profile);
  }
}
