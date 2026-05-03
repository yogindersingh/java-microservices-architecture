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
@Table(name = "cards")
@Getter
@Setter
public class Cards extends AuditData{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer cardId;

  @Column(updatable = false, nullable = false)
  @Size(min = 10, max = 10)
  String mobileNumber;

  @Column(updatable = false, nullable = false)
  String cardNumber;
  @Column(updatable = false, nullable = false)
  String cardType;
  Integer totalLimit;
  Integer amountUsed;
  Integer availableAmount;

}
