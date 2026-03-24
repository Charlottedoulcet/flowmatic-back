package com.flowmatic.flowmatic_back.exception;

public class InvalidStatusTransitionException extends RuntimeException {
  public InvalidStatusTransitionException(String message) {
    super(message);
  }
}
