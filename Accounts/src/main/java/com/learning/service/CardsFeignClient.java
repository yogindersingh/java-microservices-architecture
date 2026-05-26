package com.learning.service;

import com.learning.dto.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cards",fallback = CardsFeignClientFallback.class)
public interface CardsFeignClient {

  @GetMapping("/v1/fetch")
  public ResponseEntity<ResponseDto> fetchCard(@RequestHeader(name = "X-Correlation-ID")  String correlationId,
                                               @RequestParam String mobileNumber);

}
