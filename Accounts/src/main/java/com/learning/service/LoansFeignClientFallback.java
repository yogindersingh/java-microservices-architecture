package com.learning.service;


import com.learning.dto.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LoansFeignClientFallback implements LoansFeignClient {

  @Override
  public ResponseEntity<ResponseDto> fetchLoan(String correlationId, String mobileNumber) {
    return null;
  }
}
