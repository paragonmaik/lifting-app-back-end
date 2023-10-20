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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Program {
  @Id
  @SequenceGenerator(name = "program_sequence", sequenceName = "program_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "program_sequence")
  private Integer id;
  @Column(name = "user_id")
  private String userId;
  @NotBlank
  private String name;
  @Column(name = "duration_weeks")
  private Integer durationWeeks;
  private String description;
  @Column(name = "date_created")
  @CreationTimestamp
  private LocalDateTime dateCreated;

  @ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE,
      CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.LAZY)
  @JoinTable(name = "program_workout", joinColumns = {
      @JoinColumn(name = "program_id") }, inverseJoinColumns = {
          @JoinColumn(name = "workout_id") })
  private Set<Workout> workouts = new HashSet<>();
}
