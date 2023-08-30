package com.hoister.tonshoister.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hoister.tonshoister.DTOs.DTOsMapper;
import com.hoister.tonshoister.DTOs.ProfileDTO;
import com.hoister.tonshoister.services.ProfileService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

  @Autowired
  ProfileService profileService;
  @Autowired
  DTOsMapper DTOsMapper;

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PutMapping("/edit")
  public void updateProgram(@Valid @RequestBody ProfileDTO profileDTO) throws Exception {
    profileService
        .updateProfile(DTOsMapper.convertToEntity(profileDTO));
  }
}
