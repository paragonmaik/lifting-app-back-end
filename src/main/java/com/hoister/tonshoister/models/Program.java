package com.hoister.tonshoister.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table
public class Program {
  @Id
  @SequenceGenerator(name = "program_sequence", sequenceName = "program_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "program_sequence")
  Integer id;
  @NotBlank
  String name;
  @Column(name = "duration_weeks")
  Integer durationWeeks;
  String description;
  @Column(name = "date_created")
  @CreationTimestamp
  LocalDateTime dateCreated;

  @ManyToMany(cascade = { CascadeType.ALL })
  @JoinTable(name = "program_workout", joinColumns = {
      @JoinColumn(name = "program_id") }, inverseJoinColumns = {
          @JoinColumn(name = "workout_id") })
  Set<Workout> workouts = new HashSet<>();

  public Program() {
  }

  public Program(String name, Integer durationWeeks, String description) {
    this.name = name;
    this.durationWeeks = durationWeeks;
    this.description = description;
  }

  public Program(Integer id, String name, Integer durationWeeks, String description) {
    this.id = id;
    this.name = name;
    this.durationWeeks = durationWeeks;
    this.description = description;
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
