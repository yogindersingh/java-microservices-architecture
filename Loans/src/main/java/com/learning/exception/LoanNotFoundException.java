package com.learning.exception;

public class LoanNotFoundException extends RuntimeException {
  public LoanNotFoundException(String message, String... parm) {
        super(String.format(message,parm));
    }
}
