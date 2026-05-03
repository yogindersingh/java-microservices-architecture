package com.learning.service;


import com.learning.dto.LoanDto;

public interface ILoansService {

  void createLoan(String mobileNumber);

  LoanDto getLoan(String mobileNumber);
}
