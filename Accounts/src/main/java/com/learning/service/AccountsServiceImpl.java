package com.learning.service;

import com.learning.dto.AccountMessageDto;
import com.learning.dto.AccountsDto;
import com.learning.dto.CustomerDetailsDto;
import com.learning.dto.CustomerDto;
import com.learning.dto.ResponseDto;
import com.learning.entity.Accounts;
import com.learning.entity.Customer;
import com.learning.exceptions.CustomerAlreadyExistsException;
import com.learning.exceptions.ResourceNotFountException;
import com.learning.mapper.MapperUtil;
import com.learning.repository.AccountRepository;
import com.learning.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountsServiceImpl implements AccountsService {

  @Autowired
  CustomerRepository customerRepository;

  @Autowired
  AccountRepository accountRepository;

  @Autowired
  CardsFeignClient cardsFeignClient;

  @Autowired
  LoansFeignClient loansFeignClient;

  @Autowired
  StreamBridge streamBridge;

  /**
   *
   * @param customerDto
   * @return
   */
  @Override
  public ResponseDto createAccount(CustomerDto customerDto) {
    Customer customer = MapperUtil.mapCustomerDtoToCustomer(customerDto);
    customerRepository.findByMobileNumber(customer.getMobileNumber()).ifPresent((value)->{
      throw new CustomerAlreadyExistsException("Customer already exists with mobile number : %s",
          customerDto.getMobileNumber());
    });
    customerRepository.save(customer);
    Accounts accounts = MapperUtil.mapAccountsDtoToAccounts(customerDto.getAccountsDto());
    accounts.setCustomerId(customer.getCustomerId());
    accounts.setAccountNumber(((long) (Math.random() * 900000L) + 100000L));
    accountRepository.save(accounts);
    AccountMessageDto accountMessageDto=new AccountMessageDto(customer.getName(),customer.getEmail(),accounts.getAccountNumber());
    boolean resp = streamBridge.send("email-sms", accountMessageDto);
    log.info("successfully send email sms : {}",resp);
    return new ResponseDto("Account Created successfully", HttpStatus.CREATED);
  }

  @Override
  public ResponseDto getAccountByMobileNumber(String mobileNumber) {
    Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(()->
        new ResourceNotFountException(
        "Customer not " +
      "found" +
      " for mobileNumber ; %s",mobileNumber));
    Accounts accounts = accountRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(()->
      new ResourceNotFountException("Account Not found with mobileNumber : {}",mobileNumber)
    );
    AccountsDto accountsDto = MapperUtil.mapAccountsToAccountsDto(accounts);
    CustomerDto customerDto = MapperUtil.mapCustomerToCustomerDto(customer);
    customerDto.setAccountsDto(accountsDto);
    //fetch by mobile number using paging and sorting
//    Pageable pageable = PageRequest.of(1, 5, Sort.by("accountNumber").descending());
//    Page<Customer> resp = customerRepository.findByMobileNumber(mobileNumber,pageable);

    return new ResponseDto(customerDto,HttpStatus.OK);
  }

  @Override
  public ResponseDto getCustomerDetails(String correlationId,String mobileNumber) {
    log.info("X-Correlation-ID found : {}", correlationId);
    Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(()->
        new ResourceNotFountException(
            "Customer not " +
                "found" +
                " for mobileNumber ; %s",mobileNumber));
    Accounts accounts = accountRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(()->
        new ResourceNotFountException("Account Not found with mobileNumber : {}",mobileNumber)
    );
    AccountsDto accountsDto = MapperUtil.mapAccountsToAccountsDto(accounts);
    CustomerDetailsDto customerDetailsDto = MapperUtil.mapCustomerToCustomerDetailsDto(customer);
    customerDetailsDto.setAccountsDetails(accountsDto);
    ResponseEntity<ResponseDto> cards = cardsFeignClient.fetchCard(correlationId,customer.getMobileNumber());
    if(cards!=null && cards.getBody()!=null) {
      customerDetailsDto.setCardsDetails(cards.getBody().getBody());
    }
    ResponseEntity<ResponseDto> loans=loansFeignClient.fetchLoan(correlationId,customer.getMobileNumber());
    if(loans!=null&&loans.getBody()!=null) {
      customerDetailsDto.setLoansDetails(loans.getBody().getBody());
    }
    return new  ResponseDto(customerDetailsDto,HttpStatus.OK);
  }

}
