package com.hoister.tonshoister.services;

import org.springframework.stereotype.Service;

import com.hoister.tonshoister.models.User;

@Service
public class PrincipalService {
  private User authUser;
  private String authUserId;

  public User getAuthUser() {
    return this.authUser;
  }

  public void setAuthUser(User authUser) {
    this.authUser = authUser;
    setAuthUserId(authUser.getId());
  }

  public String getAuthUserId() {
    return this.authUserId;
  }

  public void setAuthUserId(String authUserId) {
    this.authUserId = authUserId;
  }

}
