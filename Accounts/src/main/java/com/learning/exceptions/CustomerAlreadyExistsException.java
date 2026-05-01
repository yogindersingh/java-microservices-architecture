package com.learning.exceptions;

public class CustomerAlreadyExistsException extends RuntimeException {

  public CustomerAlreadyExistsException(String message,String... param) {
    super(String.format(message,param));
  }

}
