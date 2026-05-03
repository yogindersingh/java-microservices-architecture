package com.learning.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("loansAuditAwareImpl")
public class LoansAuditListener implements AuditorAware<String>{

  @Override
  public Optional<String> getCurrentAuditor() {
    return Optional.of("Loans_MS");
  }



}
