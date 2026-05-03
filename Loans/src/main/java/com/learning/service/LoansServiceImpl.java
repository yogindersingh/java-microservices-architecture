package com.learning.service;

import com.learning.constants.LoansConstants;
import com.learning.dto.LoanDto;
import com.learning.entity.Loans;
import com.learning.exception.LoanNotFoundException;
import com.learning.repository.LoansRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class LoansServiceImpl implements ILoansService {

  @Autowired
  LoansRepository loansRepository;

  /**
   *
   * @param mobileNumber
   */
  @Override
  public void createLoan(String mobileNumber) {
    Loans loans = new Loans();
    long loanNumber = (1000000000000000L + new Random().nextInt());
    loans.setMobileNumber(mobileNumber);
    loans.setLoanNumber(String.valueOf(loanNumber));
    loans.setTotalLoan(LoansConstants.DEFAULT_LOAN_AMOUNT);
    loans.setOutstandingAmount(LoansConstants.DEFAULT_LOAN_AMOUNT);
    loans.setAmountPaid(0);
    loans.setLoanType(LoansConstants.LOAN_TYPE);
    loansRepository.save(loans);
  }

  @Override
  public LoanDto getLoan(String mobileNumber) {
    Loans loans = loansRepository.findByMobileNumber(mobileNumber)
        .orElseThrow(() -> new LoanNotFoundException("Loan not found " +
            "for " +
            "mobile : %s", mobileNumber));
    LoanDto loanDto = new LoanDto();
    loanDto.setLoanNumber(loans.getLoanNumber());
    loanDto.setTotalLoan(loans.getTotalLoan());
    loanDto.setLoanType(loans.getLoanType());
    loanDto.setMobileNumber(loans.getMobileNumber());
    loanDto.setAmountPaid(loans.getAmountPaid());
    loanDto.setOutstandingAmount(loans.getOutstandingAmount());
    return loanDto;
  }

}
