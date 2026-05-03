package com.learning;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "cardsAuditAwareImpl")
public class CardsApplication {

  public static void main(String[] args) {
		SpringApplication.run(CardsApplication.class, args);
  }

  @Bean
  OpenAPI accountsOpenAPI() {
    return new OpenAPI().info(new Info().title("Cards API").description("Cards API"));
  }
}
