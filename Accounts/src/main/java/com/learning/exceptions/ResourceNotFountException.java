package com.learning.exceptions;

public class ResourceNotFountException extends RuntimeException {

  public ResourceNotFountException(String message, String... param) {
    super(String.format(message,param));
  }

}
