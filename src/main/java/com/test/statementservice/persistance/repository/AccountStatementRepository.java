package com.test.statementservice.persistance.repository;

import com.test.statementservice.persistance.entity.AccountStatementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountStatementRepository extends JpaRepository<AccountStatementEntity, Long> {

    Optional<AccountStatementEntity> findByDocumentId(Long documentId);

    @Transactional(readOnly = true)
    Optional<AccountStatementEntity> findByDocumentIdAndUserId(Long documentId, UUID userId);

    @Transactional(readOnly = true)
    Optional<AccountStatementEntity> findByStatementChecksum(Byte[] checksum);




}
