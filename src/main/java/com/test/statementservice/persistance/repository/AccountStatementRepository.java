package com.test.statementservice.persistance.repository;

import com.test.statementservice.enums.UploadStatusEnum;
import com.test.statementservice.persistance.entity.AccountStatementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AccountStatementRepository extends JpaRepository<AccountStatementEntity, Long> {

    Optional<AccountStatementEntity> findByDocumentIdAndFileUploadStatusAndIsDeleted(Long documentId, UploadStatusEnum statusEnum, boolean deleted);


    Optional<AccountStatementEntity> findByStatementChecksumAndIsDeleted(byte[] checksum, boolean deleted);


    @Query("SELECT t FROM AccountStatementEntity t WHERE t.isDeleted = false AND t.userId = :userId AND t.fileUploadStatus = :status AND (t.createdDate >= :startTime AND t.modifiedDate <= :endTime)")
    Optional<List<AccountStatementEntity>> findByStatementsForSpecificDuration(@Param("userId") String userId, @Param("status") UploadStatusEnum status, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Modifying
    @Query("UPDATE AccountStatementEntity u SET u.isDeleted = true WHERE u.fileUploadStatus =:status")
    int deletePendingStatements(@Param("status") UploadStatusEnum status);


    Optional<List<AccountStatementEntity>> findByFileUploadStatusAndIsDeleted(UploadStatusEnum status, boolean deleted);


}
