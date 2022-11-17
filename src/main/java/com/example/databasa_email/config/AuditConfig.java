package com.example.databasa_email.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.UUID;

@Configuration
@EnableJpaAuditing
public class AuditConfig {
    @Bean
    AuditorAware<UUID> auditorAwareImp() {
        return new AuditorAwareImp();
    }
}
