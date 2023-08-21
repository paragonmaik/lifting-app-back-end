package com.hoister.tonshoister.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;

enum Goal {
  ENDURANCE,
  HYPERTROPHY,
  STRENGTH
}

public class Exercise {
  @Id
  @SequenceGenerator(name = "exercise_sequence", sequenceName = "exercise_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exercise_sequence")
  Integer id;
  @NotBlank
  String name;
  @NotBlank
  Integer load;
  @NotBlank
  Goal goal;
  @Column(name = "rest_seconds")
  Integer restSeconds;
  String instructions;
  @Column(name = "date_created")
  @CreationTimestamp
  LocalDateTime dateCreated;
}
