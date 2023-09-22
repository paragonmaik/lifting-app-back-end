package com.hoister.tonshoister.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table
public class Exercise {
  @Id
  @SequenceGenerator(name = "exercise_sequence", sequenceName = "exercise_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exercise_sequence")
  private Integer id;
  @Column(name = "user_id")
  private String userId;
  @NotBlank
  private String name;
  @NotNull
  private Integer load;
  @NotNull
  private GoalType goal;
  @Column(name = "rest_seconds")
  private Integer restSeconds;
  private String instructions;
  @Column(name = "date_created")
  @CreationTimestamp
  private LocalDateTime dateCreated;
  private Integer sets;
  private Integer reps;

  @ManyToMany(mappedBy = "exercises", fetch = FetchType.LAZY)
  private Set<Workout> workouts = new HashSet<>();

  public Exercise() {
  }

  public Exercise(Integer id, String userId, String name, Integer load, GoalType goal,
      Integer restSeconds, String instructions, LocalDateTime dateCreated, Integer sets,
      Integer reps, Set<Workout> workouts) {
    this.id = id;
    this.userId = userId;
    this.name = name;
    this.load = load;
    this.goal = goal;
    this.restSeconds = restSeconds;
    this.instructions = instructions;
    this.dateCreated = dateCreated;
    this.sets = sets;
    this.reps = reps;
    this.workouts = workouts;
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

  public Integer getSets() {
    return this.sets;
  }

  public void setSets(Integer sets) {
    this.sets = sets;
  }

  public Integer getReps() {
    return this.reps;
  }

  public void setReps(Integer reps) {
    this.reps = reps;
  }

}
