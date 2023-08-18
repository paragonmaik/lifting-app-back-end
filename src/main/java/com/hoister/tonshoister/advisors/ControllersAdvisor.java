package com.hoister.tonshoister.advisors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllersAdvisor {

  @ExceptionHandler(ProgramNotFoundException.class)
  public ResponseEntity<ErrorDetails> exceptionProgramNotFoundHandler() {
    ErrorDetails errorDetails = new ErrorDetails();
    errorDetails.setMessage("No programs found!");

    return ResponseEntity
        .status(HttpStatus.NOT_FOUND).body(errorDetails);
  }

  @ExceptionHandler(WorkoutNotFoundException.class)
  public ResponseEntity<ErrorDetails> exceptionWorkoutNotFoundHandler() {
    ErrorDetails errorDetails = new ErrorDetails();
    errorDetails.setMessage("No workouts found");

    return ResponseEntity
        .status(HttpStatus.NOT_FOUND).body(errorDetails);
  }
}