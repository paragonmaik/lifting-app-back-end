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
    errorDetails.setMessage("No workouts found!");

    return ResponseEntity
        .status(HttpStatus.NOT_FOUND).body(errorDetails);
  }

  @ExceptionHandler(ExerciseNotFoundException.class)
  public ResponseEntity<ErrorDetails> exceptionExerciseNotFoundHandler() {
    ErrorDetails errorDetails = new ErrorDetails();
    errorDetails.setMessage("No exercises found!");

    return ResponseEntity
        .status(HttpStatus.NOT_FOUND).body(errorDetails);
  }

  @ExceptionHandler(UserAlreadyRegisteredException.class)
  public ResponseEntity<ErrorDetails> exceptionUserAlreadyRegisteredHandler() {
    ErrorDetails errorDetails = new ErrorDetails();
    errorDetails.setMessage("User already registered!");
    // TODO: replace later with ambiguous message and status code

    return ResponseEntity
        .status(HttpStatus.CONFLICT).body(errorDetails);
  }

  @ExceptionHandler(ProfileNotFoundException.class)
  public ResponseEntity<ErrorDetails> exceptionProfileNotFoundHandler() {
    ErrorDetails errorDetails = new ErrorDetails();
    errorDetails.setMessage("Profile not found!");

    return ResponseEntity
        .status(HttpStatus.NOT_FOUND).body(errorDetails);
  }

  @ExceptionHandler(UnauthorizedUserException.class)
  public ResponseEntity<ErrorDetails> exceptionUnauthorizedUserHandler() {
    ErrorDetails errorDetails = new ErrorDetails();
    errorDetails.setMessage("User not allowed to access this resource.");

    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED).body(errorDetails);
  }
}
