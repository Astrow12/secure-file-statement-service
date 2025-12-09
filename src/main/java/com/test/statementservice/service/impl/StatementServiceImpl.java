package com.test.statementservice.service.impl;

import com.sun.jdi.request.DuplicateRequestException;
import com.test.statementservice.enums.UploadStatusEnum;
import com.test.statementservice.exception.DocumentException;
import com.test.statementservice.mapper.StatementMapper;
import com.test.statementservice.model.response.DocumentResponse;
import com.test.statementservice.persistance.repository.AccountStatementRepository;
import com.test.statementservice.s3integration.exception.S3IntegrationException;
import com.test.statementservice.s3integration.service.S3IntegrationService;
import com.test.statementservice.service.StatementService;
import com.test.statementservice.util.StatementCheckSumUtil;
import com.test.statementservice.web.UserStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatementServiceImpl implements StatementService {

    private final AccountStatementRepository accountStatementRepository;

    private final UserStore userStore;

    private final S3IntegrationService s3IntegrationService;
    private final StatementMapper statementMapper;


    private static final String ACCOUNT_STATEMENT = "account-statement";


    @Transactional
    @Override
    public DocumentResponse uploadAccountStatement(String fileOwner, MultipartFile statementFile) {
        log.info("Uploading statement from user: {}", userStore.getUserId());
        try {
            if (!statementFile.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {
                throw new IllegalArgumentException("Only PDF statements are allowed");
            }
            var checkSum = StatementCheckSumUtil.calculateChecksum(statementFile);
            if (isFileAlreadyUploaded(checkSum)) {
                throw new DuplicateRequestException("File already exists");
            }
            String key = getFileName(fileOwner);
            var savedAccountStatement = accountStatementRepository.save(statementMapper.convertToStatementEntity(
                    statementMapper.createAccountStatementDto(key, UploadStatusEnum.PENDING, statementFile.getOriginalFilename(), checkSum, fileOwner)));
            s3IntegrationService.uploadPdfToS3(key, statementFile.getBytes());
            savedAccountStatement.setFileUploadStatus(UploadStatusEnum.UPLOADED);
            accountStatementRepository.save(savedAccountStatement);
            return new DocumentResponse(savedAccountStatement.getDocumentId(), statementFile.getOriginalFilename());

        } catch (DuplicateRequestException ex) {
            log.error("File already has been uploaded", ex);
            throw new DocumentException(HttpStatus.CONFLICT, ex.getMessage());
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
        return fileUser + "-" + ACCOUNT_STATEMENT + "-" + accountUploadDate.getMonth() + "_" + accountUploadDate.getYear();
    }


    private boolean isFileAlreadyUploaded(byte[] checkSum) throws Exception {
        var isCheckSumFound = accountStatementRepository.findByStatementChecksum(checkSum);
        if (isCheckSumFound.isPresent()) {
            return true;
        }
        return false;
    }

}
