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

@Entity
@Table
public class Workout {
  @Id
  @SequenceGenerator(name = "workout_sequence", sequenceName = "workout_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "workout_sequence")
  Integer id;
  @NotBlank
  String name;
  @Column(name = "duration_mins")
  Integer durationMins;
  String description;
  @Column(name = "date_created")
  @CreationTimestamp
  LocalDateTime dateCreated;

  @ManyToMany(mappedBy = "workouts", fetch = FetchType.LAZY)
  Set<Program> programs = new HashSet<>();

  public Workout() {
  }

  public Workout(String name, Integer durationMins,
      String description, LocalDateTime dateCreated) {
    this.name = name;
    this.durationMins = durationMins;
    this.description = description;
    this.dateCreated = dateCreated;
  }

  public Workout(Integer id, String name, Integer durationMins,
      String description, LocalDateTime dateCreated) {
    this.id = id;
    this.name = name;
    this.durationMins = durationMins;
    this.description = description;
    this.dateCreated = dateCreated;
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
