package com.test.statementservice.persistance.listener;

import com.test.statementservice.persistance.entity.AccountStatementEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class AccountStatementListener {

    @PrePersist
    public void defaultInsertPopulation(final AccountStatementEntity accountStatementEntity) {
        log.info("Storing account statement, date time fields after save");
        try {
            accountStatementEntity.setCreatedDate(LocalDateTime.now());
            accountStatementEntity.setModifiedDate(LocalDateTime.now());
        } catch (Exception ex) {
            log.error("Failed to auto insert date time fields with ex {}", ex);
        }
    }

    @PreUpdate
    public void defaultUpdatePopulation(final AccountStatementEntity accountStatementEntity) {
        log.info("Updating account statement, date time fields after save");
        try {
            accountStatementEntity.setModifiedDate(LocalDateTime.now());
        } catch (Exception ex) {
            log.error("Failed to auto update date time fields with ex {}", ex);
        }
    }
}
