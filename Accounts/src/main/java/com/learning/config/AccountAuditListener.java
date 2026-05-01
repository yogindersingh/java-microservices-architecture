package com.learning.config;

import com.learning.dto.CustomerDto;
import jakarta.persistence.PrePersist;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditAwareImpl")
public class AccountAuditListener implements AuditorAware<String>{

  @Override
  public Optional<String> getCurrentAuditor() {
    return Optional.of("Accounts_MS");
  }



}
