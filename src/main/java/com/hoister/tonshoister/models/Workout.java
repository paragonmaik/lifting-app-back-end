package com.hoister.tonshoister.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table
public class Workout {
  @Id
  @SequenceGenerator(name = "workout_sequence", sequenceName = "workout_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "workout_sequence")
  private Integer id;
  @Column(name = "user_id")
  private String userId;
  @NotBlank
  private String name;
  @Column(name = "duration_mins")
  private Integer durationMins;
  private String description;
  @Column(name = "date_created")
  @CreationTimestamp
  private LocalDateTime dateCreated;

  @ManyToMany(mappedBy = "workouts", fetch = FetchType.LAZY)
  private Set<Program> programs = new HashSet<>();

  @ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE,
      CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.LAZY)
  @JoinTable(name = "workout_exercise", joinColumns = {
      @JoinColumn(name = "workout_id") }, inverseJoinColumns = {
          @JoinColumn(name = "exercise_id") })
  private Set<Exercise> exercises = new HashSet<>();

  public Workout() {
  }

  public Workout(Integer id, String userId, String name, Integer durationMins, String description,
      LocalDateTime dateCreated, Set<Program> programs, Set<Exercise> exercises) {
    this.id = id;
    this.userId = userId;
    this.name = name;
    this.durationMins = durationMins;
    this.description = description;
    this.dateCreated = dateCreated;
    this.programs = programs;
    this.exercises = exercises;
  }

  public String getUserId() {
    return this.userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Set<Exercise> getExercises() {
    return this.exercises;
  }

  public void setExercises(Set<Exercise> exercises) {
    this.exercises = exercises;
  }

  public Integer getId() {
    return this.id;
  }

  public Set<Program> getPrograms() {
    return this.programs;
  }

  public void setPrograms(Set<Program> programs) {
    this.programs = programs;
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

  public Integer getDurationMins() {
    return this.durationMins;
  }

  public void setDurationMins(Integer durationMins) {
    this.durationMins = durationMins;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDateTime getDateCreated() {
    return this.dateCreated;
  }

  public void setDateCreated(LocalDateTime dateCreated) {
    this.dateCreated = dateCreated;
  }
}
