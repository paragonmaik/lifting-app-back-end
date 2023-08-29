package com.hoister.tonshoister.authentication;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import com.hoister.tonshoister.DTOs.AuthenticationDTO;
import com.hoister.tonshoister.DTOs.RegisterDTO;
import com.hoister.tonshoister.advisors.UserAlreadyRegisteredException;
import com.hoister.tonshoister.models.User;
import com.hoister.tonshoister.models.UserRole;
import com.hoister.tonshoister.repositories.UserRepository;
import com.hoister.tonshoister.security.TokenService;
import com.hoister.tonshoister.services.AuthenticationService;
import com.hoister.tonshoister.services.UserService;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTests {

  @Mock
  UserRepository userRepository;
  @Mock
  UserService userService;
  @Mock
  AuthenticationManager authenticationManager;
  @Mock
  TokenService tokenService;
  @Mock
  AuthenticationService authenticationService;

  @Test
  public void registerUserSuccess() {
    User user = new User("arnold", "gettothechoppah", UserRole.ADMIN);

    authenticationService.registerUser(user);
    verify(authenticationService).registerUser(user);
  }

  @Test
  public void loginUserSuccess() {
    AuthenticationDTO loginData = new AuthenticationDTO("arnold", "gettothechoppah");

    authenticationService.loginUser(loginData);
    verify(authenticationService).loginUser(loginData);
  }
}
