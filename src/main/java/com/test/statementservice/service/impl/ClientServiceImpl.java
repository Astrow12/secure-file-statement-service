package com.test.statementservice.service.impl;

import com.test.statementservice.enums.UploadStatusEnum;
import com.test.statementservice.exception.ClientException;
import com.test.statementservice.mapper.StatementMapper;
import com.test.statementservice.model.response.SignedStatementResponse;
import com.test.statementservice.model.response.StatementsResponse;
import com.test.statementservice.persistance.repository.AccountStatementRepository;
import com.test.statementservice.s3integration.exception.S3IntegrationException;
import com.test.statementservice.s3integration.service.S3IntegrationService;
import com.test.statementservice.service.ClientService;
import com.test.statementservice.web.UserStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Service
public class ClientServiceImpl implements ClientService {

    @Value("${statement.s3-integration.signed-expiration}")
    private Duration expirationDate;

    private final AccountStatementRepository accountStatementRepository;

    private final UserStore userStore;
    private static final String DOWN_STREAM_ERROR = "Downstream error occurred";
    private static final String GENERIC_INTERNAL_ERROR = "Error occurred in statements api";

    private final S3IntegrationService s3IntegrationService;
    private final StatementMapper statementMapper;


    @Transactional(readOnly = true)
    @Override
    public SignedStatementResponse generateAccountStatementPDF(Long documentId) {
        log.info("Generating account statement for user: {}", userStore.getUserId());
        try {
            var savedAccountStatement = accountStatementRepository.findByDocumentIdAndFileUploadStatusAndIsDeleted(documentId, UploadStatusEnum.UPLOADED, false);
            if (savedAccountStatement.isPresent()) {
                var signedAccountStatement = s3IntegrationService.generateS3SignedUrl(savedAccountStatement.get().getS3StatementKey(), expirationDate);
                return new SignedStatementResponse(signedAccountStatement, documentId);
            } else {
                log.warn("Document with id {} does not exist", documentId);
                throw new FileNotFoundException("Document has not been found");
            }

        } catch (FileNotFoundException ex) {
            log.error("File does not exist", ex);
            throw new ClientException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (S3IntegrationException ex) {
            log.error("Exception occurred in s3Integration", ex);
            throw new ClientException(HttpStatus.FAILED_DEPENDENCY, DOWN_STREAM_ERROR);

        } catch (Exception ex) {
            log.error("Exception occurred while generating account statement", ex);
            throw new ClientException(HttpStatus.INTERNAL_SERVER_ERROR, GENERIC_INTERNAL_ERROR);
        }
    }

    @Override
    public StatementsResponse getAccountStatements(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Retrieving account statements for user: {}", userStore.getUserId());
        try {
            var savedAccountStatement = accountStatementRepository.findByStatementsForSpecificDuration(userStore.getUserId(), UploadStatusEnum.UPLOADED, startDate, endDate);
            if (savedAccountStatement.isPresent() && !savedAccountStatement.get().isEmpty()) {
                return StatementsResponse.builder()
                        .documents(statementMapper.convertToListOfDocumentDto(savedAccountStatement.get()))
                        .build();
            } else {
                log.warn("No account statements uploaded for user {}", userStore.getUserId());
                return new StatementsResponse();
            }
        } catch (S3IntegrationException ex) {
            log.error("Exception occurred in s3Integration", ex);
            throw new ClientException(HttpStatus.FAILED_DEPENDENCY, DOWN_STREAM_ERROR);

        } catch (Exception ex) {
            log.error("Exception occurred while generating account statement", ex);
            throw new ClientException(HttpStatus.INTERNAL_SERVER_ERROR, GENERIC_INTERNAL_ERROR);
        }
    }

}
