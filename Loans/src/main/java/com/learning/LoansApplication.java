package com.learning;


import com.learning.dto.LoansContactInfoDto;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "loansAuditAwareImpl")
@EnableConfigurationProperties(LoansContactInfoDto.class)
public class LoansApplication {

  public static void main(String[] args) {
		SpringApplication.run(LoansApplication.class, args);
  }

  @Bean
  OpenAPI accountsOpenAPI() {
    return new OpenAPI().info(new Info().title("Loans API").description("Loans API"));
  }
}
