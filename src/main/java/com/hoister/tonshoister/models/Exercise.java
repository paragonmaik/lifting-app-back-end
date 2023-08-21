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

enum GoalType {
  ENDURANCE,
  HYPERTROPHY,
  STRENGTH
}

@Entity
@Table
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
  GoalType goal;
  @Column(name = "rest_seconds")
  Integer restSeconds;
  String instructions;
  @Column(name = "date_created")
  @CreationTimestamp
  LocalDateTime dateCreated;

  @ManyToMany(mappedBy = "exercises", fetch = FetchType.LAZY)
  Set<Workout> workouts = new HashSet<>();

  public Exercise() {
  }

  public Exercise(String name, Integer load, GoalType goal, Integer restSeconds,
      String instructions) {
    this.name = name;
    this.load = load;
    this.goal = goal;
    this.restSeconds = restSeconds;
    this.instructions = instructions;
  }

  public Exercise(Integer id, String name, Integer load, GoalType goal,
      Integer restSeconds, String instructions) {
    this.id = id;
    this.name = name;
    this.load = load;
    this.goal = goal;
    this.restSeconds = restSeconds;
    this.instructions = instructions;
  }

  public Exercise(Integer id, String name, Integer load, GoalType goal, Integer restSeconds, String instructions,
      LocalDateTime dateCreated) {
    this.id = id;
    this.name = name;
    this.load = load;
    this.goal = goal;
    this.restSeconds = restSeconds;
    this.instructions = instructions;
    this.dateCreated = dateCreated;
  }

}
