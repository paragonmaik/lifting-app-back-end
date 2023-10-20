package com.hoister.tonshoister.models;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

  @Id
  @Column(name = "user_id")
  private String id;
  private Integer weight;
  private Integer height;
  @OneToOne
  @MapsId
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = false)
  @JoinColumn(name = "user_id")
  private Set<Program> programs = new HashSet<>();

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = false)
  @JoinColumn(name = "user_id")
  private Set<Workout> workouts = new HashSet<>();

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = false)
  @JoinColumn(name = "user_id")
  private Set<Exercise> exercises = new HashSet<>();

}
