package com.learning.Enum;

public enum AccountType {
  SAVINGS("savings"),
  CURRENT("current");

  private String accountType;
  AccountType(String accountType) {
    this.accountType = accountType;
  }

  public String getAccountType() {
    return accountType;
  }

}
