package com.hoister.tonshoister.models;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "profile")
public class Profile {

  @Id
  @Column(name = "user_id")
  private String id;
  private Integer weight;
  private Integer height;
  @OneToOne
  @MapsId
  @JoinColumn(name = "user_id")
  private User user;

  public Profile() {
  }

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "user_id")
  private Set<Program> programs = new HashSet<>();

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "user_id")
  private Set<Workout> workouts = new HashSet<>();

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "user_id")
  private Set<Exercise> exercises = new HashSet<>();

  public Profile(Integer weight, Integer height) {
    this.weight = weight;
    this.height = height;
  }

  public Profile(String id, Integer weight, Integer height, User user,
      Set<Program> programs, Set<Workout> workouts,
      Set<Exercise> exercises) {
    this.id = id;
    this.weight = weight;
    this.height = height;
    this.user = user;
    this.programs = programs;
    this.workouts = workouts;
    this.exercises = exercises;
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
