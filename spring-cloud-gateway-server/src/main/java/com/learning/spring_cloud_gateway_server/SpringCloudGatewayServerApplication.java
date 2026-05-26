package com.learning.spring_cloud_gateway_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.time.Duration;
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
                    .addResponseHeader("X-response-time", LocalDateTime.now().toString())
                    .circuitBreaker(config -> config.setName("accountscircuitbreaker")
                        .setFallbackUri("forward:/contactsupport"))
            )
            .uri("lb://ACCOUNTS")
    ).route(route ->
        route.path("/bank/cards/**")
            .filters(gatewayFilterSpec -> gatewayFilterSpec.rewritePath("/bank/cards/(?<segments>.*)",
                    "/${segments}").addResponseHeader("Content-Type", "application/json")
                .addResponseHeader("X-response-time", LocalDateTime.now().toString())
                .retry(retryConfig -> retryConfig.setRetries(3).setMethods(HttpMethod.GET)
                    .setBackoff(Duration.ofMillis(100),
                        Duration.ofMillis(1000), 2,
                        true)))
            .uri("lb://CARDS")
    ).route(route ->
        route.path("/bank/loans/**")
            .filters(gatewayFilterSpec -> gatewayFilterSpec.rewritePath("/bank/loans/(?<segments>.*)", "/${segments}")
                .addResponseHeader("Content-Type", "application/json")
                .addResponseHeader("X-response-time", LocalDateTime.now().toString())
                .requestRateLimiter(config -> config.setRateLimiter(redisRateLimiter()).setKeyResolver(keyResolver())))
            .uri("lb://LOANS")
    ).build();
  }

  @Bean
  RedisRateLimiter redisRateLimiter(){
    return new RedisRateLimiter(1,1,1);
  }

  @Bean
  KeyResolver keyResolver() {
    return exchange->Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("segments")).defaultIfEmpty(
        "anonymous");
  }

}
