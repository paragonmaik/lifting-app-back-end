package com.hoister.tonshoister.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hoister.tonshoister.DTOs.AuthenticationDTO;
import com.hoister.tonshoister.advisors.UserAlreadyRegisteredException;
import com.hoister.tonshoister.models.User;
import com.hoister.tonshoister.security.TokenService;

@Service
public class AuthenticationService {

  @Autowired
  private UserService userService;
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private TokenService tokenService;

  public void registerUser(User user) {
    if (userService.loadUserByUsername(user.getLogin()) != null) {
      throw new UserAlreadyRegisteredException();
    }

    String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
    User newUser = new User(user.getLogin(), encryptedPassword, user.getRole());

    userService.saveUser(newUser);
  }

  public String loginUser(AuthenticationDTO data) {
    UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
        data.login(), data.password());
    Authentication auth = authenticationManager.authenticate(usernamePassword);

    return tokenService.generateToken((User) auth.getPrincipal());
  }
}
