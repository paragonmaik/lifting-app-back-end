package com.hoister.tonshoister.authentication;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;

import com.hoister.tonshoister.DTOs.AuthenticationDTO;
import com.hoister.tonshoister.advisors.UserAlreadyRegisteredException;
import com.hoister.tonshoister.models.*;
import com.hoister.tonshoister.repositories.UserRepository;
import com.hoister.tonshoister.security.TokenService;
import com.hoister.tonshoister.services.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTests {

  @Mock
  Authentication auth;
  @Mock
  ProfileService profileService;
  @Mock
  UserRepository userRepository;
  @Mock
  UserService userService;
  @Mock
  AuthenticationManager authenticationManager;
  @Mock
  TokenService tokenService;
  @InjectMocks
  AuthenticationService authenticationService;

  @Test
  public void registerUserSuccess() {
    User user = new User("arnold", "gettothechoppah", UserRole.ADMIN);

    when(userService.loadUserByUsername(user.getLogin())).thenReturn(null);
    authenticationService.registerUser(user);

    verify(userService).loadUserByUsername(user.getLogin());
  }

  @Test
  public void registerUserThrowsException() {
    User user = new User("arnold", "gettothechoppah", UserRole.ADMIN);

    when(userService.loadUserByUsername(user.getLogin())).thenReturn(user);
    assertThrows(UserAlreadyRegisteredException.class, () -> {
      authenticationService.registerUser(user);
    });

    verify(userService).loadUserByUsername(user.getLogin());
  }

  @Test
  public void loginUserSuccess() {
    User user = new User("arnold", "gettothechoppah", UserRole.ADMIN);
    AuthenticationDTO loginData = new AuthenticationDTO(user.getLogin(), user.getPassword());
    var usernamePassword = new UsernamePasswordAuthenticationToken(
        loginData.login(), loginData.password());

    when(authenticationManager.authenticate(usernamePassword)).thenReturn(auth);
    when(tokenService.generateToken(user)).thenReturn("token");
    when(auth.getPrincipal()).thenReturn(user);
    authenticationService.loginUser(loginData);

    verify(tokenService).generateToken(user);
  }
}
