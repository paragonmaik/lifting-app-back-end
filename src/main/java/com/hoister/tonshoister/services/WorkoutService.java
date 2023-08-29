package com.hoister.tonshoister.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoister.tonshoister.advisors.ProgramNotFoundException;
import com.hoister.tonshoister.advisors.WorkoutNotFoundException;
import com.hoister.tonshoister.models.Program;
import com.hoister.tonshoister.models.Workout;
import com.hoister.tonshoister.repositories.ProgramRepository;
import com.hoister.tonshoister.repositories.WorkoutRepository;

@Service
public class WorkoutService {

  @Autowired
  private WorkoutRepository workoutRepository;
  @Autowired
  private ProgramRepository programRepository;

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

  public List<Workout> findAll() {
    List<Workout> workouts = workoutRepository.findAll();

    if (workouts.isEmpty()) {
      throw new WorkoutNotFoundException();
    }

    return workouts;
  }

  public Workout updateWorkout(Workout workout) throws WorkoutNotFoundException {
    Workout foundWorkout = workoutRepository.findById(workout.getId())
        .orElseThrow(() -> new WorkoutNotFoundException());

    foundWorkout.setName(workout.getName());
    foundWorkout.setDescription(workout.getDescription());
    foundWorkout.setDurationMins(workout.getDurationMins());

    return workoutRepository.save(foundWorkout);
  }

  public void deleteWorkout(Integer id) throws WorkoutNotFoundException {
    if (!workoutRepository.existsById(id)) {
      throw new WorkoutNotFoundException();
    }

    workoutRepository.deleteById(id);
  }
}
