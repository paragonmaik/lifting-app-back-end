package com.hoister.tonshoister.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Exercise {
  @Id
  @SequenceGenerator(name = "exercise_sequence", sequenceName = "exercise_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exercise_sequence")
  private Integer id;
  @Column(name = "user_id")
  private String userId;
  @NotBlank
  private String name;
  @NotNull
  private Integer load;
  @NotNull
  private GoalType goal;
  @Column(name = "rest_seconds")
  private Integer restSeconds;
  private String instructions;
  @Column(name = "date_created")
  @CreationTimestamp
  private LocalDateTime dateCreated;
  private Integer sets;
  private Integer reps;
  @Column(name = "exec_order")
  private Integer execOrder;

  @ManyToMany(mappedBy = "exercises", fetch = FetchType.LAZY)
  private Set<Workout> workouts = new HashSet<>();
}
