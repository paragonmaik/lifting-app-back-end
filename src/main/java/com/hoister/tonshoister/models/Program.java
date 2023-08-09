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
}
