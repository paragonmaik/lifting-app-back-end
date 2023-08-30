package com.hoister.tonshoister.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "profile")
public class Profile {

  @Id
  @Column(name = "user_id")
  String id;
  Integer weight;
  Integer height;

  @OneToOne
  @MapsId
  @JoinColumn(name = "user_id")
  private User user;

  public Profile() {
  }

  public Profile(String id, Integer weight, Integer height, User user) {
    this.id = id;
    this.weight = weight;
    this.height = height;
    this.user = user;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Integer getWeight() {
    return this.weight;
  }

  public void setWeight(Integer weight) {
    this.weight = weight;
  }

  public Integer getHeight() {
    return this.height;
  }

  public void setHeight(Integer height) {
    this.height = height;
  }

  public User getUser() {
    return this.user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
