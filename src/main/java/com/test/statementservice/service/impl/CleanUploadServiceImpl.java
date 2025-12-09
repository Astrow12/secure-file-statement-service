package com.test.statementservice.service.impl;

import com.test.statementservice.enums.UploadStatusEnum;
import com.test.statementservice.persistance.repository.AccountStatementRepository;
import com.test.statementservice.s3integration.service.S3IntegrationService;
import com.test.statementservice.service.CleanUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CleanUploadServiceImpl implements CleanUploadService {

    private final AccountStatementRepository accountStatementRepository;

    private final S3IntegrationService s3IntegrationService;

    @Scheduled(cron = "${scheduler.cron.delete-pending-uploads}")
    @SchedulerLock(name = "deleteAllPendingUploadLock", lockAtLeastFor = "PT30S", lockAtMostFor = "PT1H")
    //Used to maintain deletes in horizontal scaled applications
    @Transactional
    @Override
    @Async
    public void cleanUpPendingUploads() {
        log.info("Deleting all pending uploads as per schedule");
        try {
            var pendingStatements = accountStatementRepository.findByFileUploadStatusAndIsDeleted(UploadStatusEnum.PENDING, false);
            if (pendingStatements.isPresent() && !pendingStatements.get().isEmpty()) {
                pendingStatements.get().stream().forEach(statementEntity -> {
                    log.info("Deleting statements in s3 with key {}", statementEntity.getS3StatementKey());
                    s3IntegrationService.deletePdfOnS3(statementEntity.getS3StatementKey());
                });
            }
            var deletedStatements = accountStatementRepository.deletePendingStatements(UploadStatusEnum.PENDING);
            log.info("Deleted statements in db with pending status");

            log.info("Deleted pending uploads in the system");
        } catch (Exception ex) {
            log.error("Exception occurred while deleting pending uploads", ex);
            throw new RuntimeException(ex.getMessage());
        }

    }

}
