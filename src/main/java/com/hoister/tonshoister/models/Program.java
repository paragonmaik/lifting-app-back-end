package com.hoister.tonshoister.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table
public class Program {
  @Id
  @SequenceGenerator(name = "program_sequence", sequenceName = "program_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "program_sequence")
  private Integer id;
  @Column(name = "user_id")
  private String userId;
  @NotBlank
  private String name;
  @Column(name = "duration_weeks")
  private Integer durationWeeks;
  private String description;
  @Column(name = "date_created")
  @CreationTimestamp
  private LocalDateTime dateCreated;

  @ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE,
      CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.LAZY)
  @JoinTable(name = "program_workout", joinColumns = {
      @JoinColumn(name = "program_id") }, inverseJoinColumns = {
          @JoinColumn(name = "workout_id") })
  private Set<Workout> workouts = new HashSet<>();

  public Program() {
  }

  public Program(Integer id, String userId, String name, Integer durationWeeks, String description,
      LocalDateTime dateCreated, Set<Workout> workouts) {
    this.id = id;
    this.userId = userId;
    this.name = name;
    this.durationWeeks = durationWeeks;
    this.description = description;
    this.dateCreated = dateCreated;
    this.workouts = workouts;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUserId() {
    return this.userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Set<Workout> getWorkouts() {
    return this.workouts;
  }

  public void setWorkouts(Set<Workout> workouts) {
    this.workouts = workouts;
  }

  public Integer getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getDurationWeeks() {
    return this.durationWeeks;
  }

  public void setDurationWeeks(Integer durationWeeks) {
    this.durationWeeks = durationWeeks;
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
