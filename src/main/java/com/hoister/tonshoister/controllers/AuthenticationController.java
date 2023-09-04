package com.hoister.tonshoister.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hoister.tonshoister.DTOs.AuthenticationDTO;
import com.hoister.tonshoister.DTOs.DTOsMapper;
import com.hoister.tonshoister.DTOs.LoginResponseDTO;
import com.hoister.tonshoister.DTOs.RegisterDTO;
import com.hoister.tonshoister.services.AuthenticationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

  @Autowired
  private AuthenticationService authenticationService;
  @Autowired
  private DTOsMapper DTOsMapper;

  @PostMapping("/register")
  public ResponseEntity<Void> register(@RequestBody @Valid RegisterDTO data) {
    authenticationService.registerUser(
        DTOsMapper.convertToEntity(data));

    return ResponseEntity.ok().build();
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
    String token = authenticationService.loginUser(data);

    return ResponseEntity.ok(new LoginResponseDTO(token));
  }
}
