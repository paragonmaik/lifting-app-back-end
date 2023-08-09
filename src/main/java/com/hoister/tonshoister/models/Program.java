package com.hoister.tonshoister.models;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Column;

public class Program {
  @Id
  Integer id;
  String name;
  @Column(name = "duration_weeks")
  Integer durationWeeks;
  String description;

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
