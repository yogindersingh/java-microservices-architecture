package com.learning.service;


import com.learning.dto.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CardsFeignClientFallback implements CardsFeignClient {

  @Override
  public ResponseEntity<ResponseDto> fetchCard(String correlationId, String mobileNumber) {
    return null;
  }
}
