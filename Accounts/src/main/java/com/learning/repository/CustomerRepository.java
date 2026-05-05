package com.learning.repository;

import com.learning.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    public Optional<Customer> findByMobileNumber(String mobileNo);

    //Fetch by mobile number pageable content
    public Page<Customer> findByMobileNumber(String mobileNo, Pageable pageable);

}
