package com.learning.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "Card")
public class CardsDto {
  String mobileNumber;
  String cardNumber;
  String cardType;
  Integer totalLimit;
  Integer amountUsed;
  Integer availableAmount;
}
