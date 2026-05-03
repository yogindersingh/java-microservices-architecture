package com.learning.exception;

public class CardNotFoundException extends RuntimeException {
  public CardNotFoundException(String message,String... parm) {
        super(String.format(message,parm));
    }
}
