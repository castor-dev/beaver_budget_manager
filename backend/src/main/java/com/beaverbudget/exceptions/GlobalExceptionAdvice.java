package com.beaverbudget.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionAdvice {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(FeatureNotImplementedException.class)
  public ResponseEntity<ErrorMessage> handleFeatureNotAvailable(FeatureNotImplementedException ex) {
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
        .body(new ErrorMessage(ex.getMessage()));
  }

  @ExceptionHandler(InvalidResourceException.class)
    public ResponseEntity<ErrorMessage> handleInvalidResourceException(InvalidResourceException ex){
      return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessage(ex.getMessage()));
  }
}
