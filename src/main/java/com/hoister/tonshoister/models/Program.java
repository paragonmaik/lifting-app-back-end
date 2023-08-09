package com.hoister.tonshoister.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table
public class Program {
  @Id
  @SequenceGenerator(name = "program_sequence", sequenceName = "program_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "program_sequence")
  Integer id;
  String name;
  @Column(name = "duration_weeks")
  Integer durationWeeks;
  String description;
  @Column(name = "date_created")
  LocalDateTime dateCreated;

  public Program() {
  }

  public Program(String name, Integer durationWeeks, String description) {
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

}
