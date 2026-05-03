package com.learning.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "loans")
@Getter
@Setter
public class Loans extends AuditData{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer loanId;

  @Column(updatable = false, nullable = false)
  @Size(min = 10, max = 10)
  String mobileNumber;

  @Column(updatable = false, nullable = false)
  String loanNumber;
  @Column(updatable = false, nullable = false)
  String loanType;
  Integer totalLoan;
  Integer amountPaid;
  Integer outstandingAmount;

}
