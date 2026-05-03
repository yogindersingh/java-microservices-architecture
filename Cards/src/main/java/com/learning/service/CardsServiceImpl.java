package com.learning.service;

import com.learning.constants.CardsConstants;
import com.learning.dto.CardsDto;
import com.learning.entity.Cards;
import com.learning.exception.CardNotFoundException;
import com.learning.repository.CardsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CardsServiceImpl implements ICardsService{

  @Autowired
  CardsRepository cardsRepository;


  /**
   *
   * @param mobileNumber
   */
  @Override
  public void createCard(String mobileNumber) {
    Cards cards = new Cards();
    long cardNumber = (1000000000000000L + new Random().nextInt());
    cards.setMobileNumber(mobileNumber);
    cards.setCardNumber(String.valueOf(cardNumber));
    cards.setCardType(CardsConstants.CARD_TYPE);
    cards.setAmountUsed(0);
    cards.setAvailableAmount(CardsConstants.DEFAULT_AMOUNT);
    cards.setTotalLimit(CardsConstants.DEFAULT_AMOUNT);
    cardsRepository.save(cards);
  }

  @Override
  public CardsDto getCard(String mobileNumber) {
    Cards cards = cardsRepository.findByMobileNumber(mobileNumber)
        .orElseThrow(() -> new CardNotFoundException("Card not found " +
            "for " +
            "mobile : %s", mobileNumber));
    CardsDto cardsDto = new CardsDto();
    cardsDto.setCardNumber(cards.getCardNumber());
    cardsDto.setCardType(cards.getCardType());
    cardsDto.setAmountUsed(cards.getAmountUsed());
    cardsDto.setAvailableAmount(cards.getAvailableAmount());
    cardsDto.setTotalLimit(cards.getTotalLimit());
    cardsDto.setMobileNumber(cards.getMobileNumber());
    return cardsDto;
  }

}
