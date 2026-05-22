package com.learning.service;


import com.learning.dto.CustomerDto;
import com.learning.dto.ResponseDto;
import org.springframework.web.bind.annotation.RequestBody;

public interface AccountsService {
  ResponseDto createAccount(
      @RequestBody
      CustomerDto customerDto);

  ResponseDto getAccountByMobileNumber(String mobileNumber);

  ResponseDto getCustomerDetails(String mobileNumber);
}
