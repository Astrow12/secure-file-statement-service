package com.test.statementservice.configuration;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableSchedulerLock(defaultLockAtMostFor = "PT1H")
@EnableScheduling
public class ShedLockConfiguration {

    @Bean
    public LockProvider lockProvider(final JdbcTemplate db) {
        return new JdbcTemplateLockProvider(JdbcTemplateLockProvider.Configuration.builder()
                .withJdbcTemplate(db)
                .withTableName("statementsdb.shedlock").build());
    }
}
