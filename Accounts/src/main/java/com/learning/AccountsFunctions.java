package com.learning;

import com.learning.dto.AccountMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class AccountsFunctions {

  @Bean
  public Consumer<Long> messageConsumer() {
    return accountNumber -> {
      log.info("messages and sms were sent successfully for accountNumber :{}",accountNumber);
    };
  }
}
