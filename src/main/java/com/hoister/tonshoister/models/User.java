package com.hoister.tonshoister.models;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  private String login;
  private String password;
  private UserRole role;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAuthorities'");
  }

  @Override
  public String getPassword() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getPassword'");
  }

  @Override
  public String getUsername() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getUsername'");
  }

  @Override
  public boolean isAccountNonExpired() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'isAccountNonExpired'");
  }

  @Override
  public boolean isAccountNonLocked() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'isAccountNonLocked'");
  }

  @Override
  public boolean isCredentialsNonExpired() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'isCredentialsNonExpired'");
  }

  @Override
  public boolean isEnabled() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'isEnabled'");
  }

}
