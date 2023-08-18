package com.hoister.tonshoister.services;

import org.springframework.stereotype.Service;

import com.hoister.tonshoister.advisors.ProgramNotFoundException;
import com.hoister.tonshoister.models.Program;
import com.hoister.tonshoister.models.Workout;
import com.hoister.tonshoister.repositories.ProgramRepository;
import com.hoister.tonshoister.repositories.WorkoutRepository;

@Service
public class WorkoutService {
  private final WorkoutRepository workoutRepository;
  private final ProgramRepository programRepository;

  public WorkoutService(WorkoutRepository workoutRepository,
      ProgramRepository programRepository) {
    this.workoutRepository = workoutRepository;
    this.programRepository = programRepository;
  }

  public Workout createWorkout(Workout workout, Integer programId)
      throws ProgramNotFoundException {

    Program program = programRepository.findById(programId)
        .orElseThrow(() -> new ProgramNotFoundException());
    Workout createdWorkout = workoutRepository.save(workout);

    createdWorkout.getPrograms().add(program);
    workoutRepository.save(createdWorkout);
    program.getWorkouts().add(createdWorkout);
    programRepository.save(program);

    return createdWorkout;
  }
}
