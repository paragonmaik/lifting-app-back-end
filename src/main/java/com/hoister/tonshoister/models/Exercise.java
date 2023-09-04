package com.hoister.tonshoister.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table
public class Exercise {
  @Id
  @SequenceGenerator(name = "exercise_sequence", sequenceName = "exercise_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exercise_sequence")
  Integer id;
  @Column(name = "user_id")
  String userId;
  @NotBlank
  String name;
  @NotNull
  Integer load;
  @NotNull
  GoalType goal;
  @Column(name = "rest_seconds")
  Integer restSeconds;
  String instructions;
  @Column(name = "date_created")
  @CreationTimestamp
  LocalDateTime dateCreated;

  @ManyToMany(mappedBy = "exercises", fetch = FetchType.LAZY)
  Set<Workout> workouts = new HashSet<>();

  public Exercise() {
  }

  public Exercise(String name, Integer load, GoalType goal, Integer restSeconds,
      String instructions) {
    this.name = name;
    this.load = load;
    this.goal = goal;
    this.restSeconds = restSeconds;
    this.instructions = instructions;
  }

  public Exercise(Integer id, String name, Integer load, GoalType goal,
      Integer restSeconds, String instructions) {
    this.id = id;
    this.name = name;
    this.load = load;
    this.goal = goal;
    this.restSeconds = restSeconds;
    this.instructions = instructions;
  }

  public Exercise(Integer id, String name, Integer load, GoalType goal,
      Integer restSeconds, String instructions,
      LocalDateTime dateCreated) {
    this.id = id;
    this.name = name;
    this.load = load;
    this.goal = goal;
    this.restSeconds = restSeconds;
    this.instructions = instructions;
    this.dateCreated = dateCreated;
  }

  public String getUserId() {
    return this.userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Integer getId() {
    return this.id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getLoad() {
    return this.load;
  }

  public void setLoad(Integer load) {
    this.load = load;
  }

  public GoalType getGoal() {
    return this.goal;
  }

  public void setGoal(GoalType goal) {
    this.goal = goal;
  }

  public Integer getRestSeconds() {
    return this.restSeconds;
  }

  public void setRestSeconds(Integer restSeconds) {
    this.restSeconds = restSeconds;
  }

  public String getInstructions() {
    return this.instructions;
  }

  public void setInstructions(String instructions) {
    this.instructions = instructions;
  }

  public LocalDateTime getDateCreated() {
    return this.dateCreated;
  }

  public void setDateCreated(LocalDateTime dateCreated) {
    this.dateCreated = dateCreated;
  }

  public Set<Workout> getWorkouts() {
    return this.workouts;
  }

  public void setWorkouts(Set<Workout> workouts) {
    this.workouts = workouts;
  }
}
