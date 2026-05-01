package com.learning.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Accounts extends AuditData{
  Long customerId;

  @Id
  @Column(updatable = false)
  Long accountNumber;

  @Column(updatable = false)
  String accountType;

  @Column(updatable = false)
  String branchAddress;
}
