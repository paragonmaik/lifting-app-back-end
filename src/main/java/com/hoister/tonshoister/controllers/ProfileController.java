package com.hoister.tonshoister.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

  @GetMapping()
  public ProfileDTO getProfileById() throws Exception {
    return DTOsMapper.convertToDto(profileService.findById());
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PutMapping("/edit")
  public void updateProfile(@Valid @RequestBody ProfileDTO profileDTO) throws Exception {
    profileService
        .updateProfile(DTOsMapper.convertToEntity(profileDTO));
  }
}
