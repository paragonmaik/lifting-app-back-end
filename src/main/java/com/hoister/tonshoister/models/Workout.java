package com.hoister.tonshoister.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

}
