package com.learning.repository;

import com.learning.entity.Accounts;
import com.learning.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Accounts, Long> {

  Optional<Accounts> findByCustomerId(Long customerId);
}
