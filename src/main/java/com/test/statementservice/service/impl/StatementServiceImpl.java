package com.test.statementservice.service.impl;

import com.test.statementservice.exception.DocumentException;
import com.test.statementservice.model.response.MessageResponse;
import com.test.statementservice.persistance.repository.AccountStatementRepository;
import com.test.statementservice.s3integration.exception.S3IntegrationException;
import com.test.statementservice.s3integration.service.S3IntegrationService;
import com.test.statementservice.service.StatementService;
import com.test.statementservice.web.UserStore;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatementServiceImpl implements StatementService {

    private final AccountStatementRepository accountStatementRepository;

    private final UserStore userStore;

    private final S3IntegrationService s3IntegrationService;


    private static final String ACCOUNT_STATEMENT = "account-statement";

    @PersistenceContext
    private EntityManager entityManager;


    public MessageResponse uploadAccountStatement(String fileOwner, MultipartFile statementFile) {
        log.info("Uploading statement from user: {}", userStore.getUserName());
        try {
            if (!statementFile.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {
                throw new IllegalArgumentException("Only PDF statements are allowed");
            }
            String key = getFileName(fileOwner);
            s3IntegrationService.uploadPdfToS3(key, statementFile.getBytes());
            accountStatementRepository.save();
            return new MessageResponse("File uploaded successfully: " + key);
        } catch (S3IntegrationException ex) {
            log.error("Exception occurred in s3Integration", ex);
            throw new DocumentException(ex.getStatus(), ex.getMessage());

        } catch (Exception ex) {
            log.error("Exception occurred while uploading account statement", ex);
            throw new DocumentException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    private String getFileName(String fileUser) {
        log.info("Generate unique filename for owner {}", fileUser);
        var accountUploadDate = LocalDate.now();
        return fileUser + "-" + ACCOUNT_STATEMENT + "-" + accountUploadDate.getDayOfMonth() + "_" + accountUploadDate.getYear();
    }


}
