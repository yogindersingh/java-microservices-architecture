package com.learning.repository;

import com.learning.entity.Cards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardsRepository extends JpaRepository<Cards, Integer> {

  Optional<Cards> findByMobileNumber(String mobileNumber);
}
