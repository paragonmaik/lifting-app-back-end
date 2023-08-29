package com.hoister.tonshoister.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.hoister.tonshoister.models.User;
import com.hoister.tonshoister.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

  @Autowired
  UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) {
    return userRepository.findByLogin(username);
  }

  public void saveUser(User user) {
    userRepository.save(user);
  }
}
