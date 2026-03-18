package com.flowmatic.flowmatic_back.exception;

public class DuplicateResourceException extends RuntimeException {
  public DuplicateResourceException(String message) {
    super(message);
  }
}
