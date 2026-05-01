package com.learning.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customer")
public class Customer extends AuditData{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long customerId;
  String name;
  String email;
  String mobileNumber;
}
