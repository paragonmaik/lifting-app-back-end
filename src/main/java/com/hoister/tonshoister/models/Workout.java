package com.hoister.tonshoister.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table
public class Workout {
  @Id
  @SequenceGenerator(name = "workout_sequence", sequenceName = "workout_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "workout_sequence")
  Integer id;
  @NotBlank
  String name;
  @Column(name = "duration_secs")
  Integer durationSecs;
  String description;
  @Column(name = "date_created")
  @CreationTimestamp
  LocalDateTime dateCreated;

  @ManyToMany(mappedBy = "workouts")
  Set<Program> programs = new HashSet<>();

  public Workout() {
  }

  public Workout(Integer id, String name, Integer durationSecs, String description, LocalDateTime dateCreated) {
    this.id = id;
    this.name = name;
    this.durationSecs = durationSecs;
    this.description = description;
    this.dateCreated = dateCreated;
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

  public Integer getDurationSecs() {
    return this.durationSecs;
  }

  public void setDurationSecs(Integer durationSecs) {
    this.durationSecs = durationSecs;
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