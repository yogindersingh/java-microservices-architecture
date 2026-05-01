package com.learning.mapper;

import com.learning.Enum.AccountType;
import com.learning.dto.AccountsDto;
import com.learning.dto.CustomerDto;
import com.learning.entity.Accounts;
import com.learning.entity.Customer;

public class MapperUtil {

  public static Customer mapCustomerDtoToCustomer(CustomerDto customerDto) {
    Customer customer = new Customer();
    customer.setName(customerDto.getName());
    customer.setEmail(customerDto.getEmail());
    customer.setMobileNumber(customerDto.getMobileNumber());
    return customer;
  }

  public static CustomerDto mapCustomerToCustomerDto(Customer customer) {
    CustomerDto customerDto = new CustomerDto();
    customerDto.setName(customer.getName());
    customerDto.setEmail(customer.getEmail());
    customerDto.setMobileNumber(customer.getMobileNumber());
    return customerDto;
  }

  public static Accounts mapAccountsDtoToAccounts(AccountsDto accountsDto) {
    Accounts accounts = new Accounts();
    accounts.setAccountType(accountsDto.getAccountType().getAccountType());
    accounts.setBranchAddress(accountsDto.getBranchAddress());
    return accounts;
  }

  public static AccountsDto mapAccountsToAccountsDto(Accounts accounts) {
    AccountsDto accountsDto = new AccountsDto();
    accountsDto.setAccountType(AccountType.valueOf(accounts.getAccountType().toUpperCase()));
    accountsDto.setBranchAddress(accounts.getBranchAddress());
    return accountsDto;
  }


}
