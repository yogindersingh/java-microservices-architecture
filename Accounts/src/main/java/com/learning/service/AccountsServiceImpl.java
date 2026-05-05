package com.learning.service;

import com.learning.dto.AccountsDto;
import com.learning.dto.CustomerDto;
import com.learning.dto.ResponseDto;
import com.learning.entity.Accounts;
import com.learning.entity.Customer;
import com.learning.exceptions.CustomerAlreadyExistsException;
import com.learning.exceptions.ResourceNotFountException;
import com.learning.mapper.MapperUtil;
import com.learning.repository.AccountRepository;
import com.learning.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AccountsServiceImpl implements AccountsService {

  @Autowired
  CustomerRepository customerRepository;

  @Autowired
  AccountRepository accountRepository;

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
    accounts.setAccountNumber(((long) (Math.random() * 9000000000L) + 1000000000L));
    accountRepository.save(accounts);
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

}
