package com.beaverbudget.exceptions;

public class InvalidResourceException extends RuntimeException {

  public InvalidResourceException(String message) {
    super(message);
  }
}
