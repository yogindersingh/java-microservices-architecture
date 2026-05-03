package com.learning.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "Loan")
public class LoanDto {
  String mobileNumber;
  String loanNumber;
  String loanType;
  Integer totalLoan;
  Integer amountPaid;
  Integer outstandingAmount;
}
