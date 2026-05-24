package com.learning.spring_cloud_gateway_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class SpringCloudGatewayServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringCloudGatewayServerApplication.class, args);
  }

  @Bean
  public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    return builder.routes().route(route ->
        route.path("/bank/accounts/**")
            .filters(
                gatewayFilterSpec -> gatewayFilterSpec.rewritePath("/bank/accounts/(?<segments>.*)", "/${segments}")
                    .addResponseHeader("Content-Type", "application/json")
                    .addResponseHeader("X-response-time", LocalDateTime.now().toString()))
            .uri("lb://ACCOUNTS")
    ).route(route ->
        route.path("/bank/cards/**")
            .filters(gatewayFilterSpec -> gatewayFilterSpec.rewritePath("/bank/cards/(?<segments>.*)",
                "/${segments}").addResponseHeader("Content-Type", "application/json")
                .addResponseHeader("X-response-time", LocalDateTime.now().toString()))
            .uri("lb://CARDS")
    ).route(route ->
        route.path("/bank/loans/**")
            .filters(gatewayFilterSpec -> gatewayFilterSpec.rewritePath("/bank/loans/(?<segments>.*)", "/${segments}")
                .addResponseHeader("Content-Type", "application/json")
                .addResponseHeader("X-response-time", LocalDateTime.now().toString()))
            .uri("lb://LOANS")
    ).build();
  }


}
