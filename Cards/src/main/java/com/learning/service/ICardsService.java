package com.learning.service;


import com.learning.dto.CardsDto;

public interface ICardsService {

  void createCard(String mobileNumber);

  CardsDto getCard(String mobileNumber);
}
