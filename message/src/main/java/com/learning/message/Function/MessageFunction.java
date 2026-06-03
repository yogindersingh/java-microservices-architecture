package com.learning.message.Function;

import com.learning.message.Dto.AccountDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
@Slf4j
public class MessageFunction {

  @Bean
  public Function<AccountDto,AccountDto> email(){
    return accountDto -> {
      log.info("email sent successfully for accountDto : {}", accountDto);
    return accountDto;
    };
  }

  @Bean
  public Function<AccountDto,Long> sms(){
    return AccountDto->{
      log.info("sms sent successfully for accountDto : {}", AccountDto);
      return AccountDto.accountId();
    };
  }

}
