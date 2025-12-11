package com.test.statementservice.accountstatement;


import com.test.statementservice.enums.UploadStatusEnum;
import com.test.statementservice.model.dto.AccountStatementDto;
import com.test.statementservice.model.response.DocumentResponse;
import com.test.statementservice.persistance.entity.AccountStatementEntity;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;


public final class StatementApiTestUtils {

    public static final Long COMMON_DOC_ID = 1L;
    public static final String COMMON_DOC_NAME = "Test file";
    public static final String TEST_CONTENT = "TestContent";
    public static final String COMMON_USER_ID = "TshepoM";


    public static DocumentResponse getSuccessDocumentResponse() {
        return DocumentResponse.builder()
                .documentId(COMMON_DOC_ID)
                .fileName(COMMON_DOC_NAME)
                .build();
    }

    public static AccountStatementEntity getEmptyAccountStatementEntity() {
        return null;
    }

    public static AccountStatementEntity getPendingAccountStatementEntity() throws IOException {
        return AccountStatementEntity.builder()
                .s3StatementKey(getFileName())
                .statementChecksum(successMultipartResponse().getBytes())
                .statementFileName(COMMON_DOC_NAME)
                .userId(COMMON_USER_ID)
                .isDeleted(false)
                .fileUploadStatus(UploadStatusEnum.PENDING)
                .documentId(1L)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();
    }

    public static AccountStatementEntity getUploadedAccountStatementEntity() throws IOException {
        return AccountStatementEntity.builder()
                .s3StatementKey(getFileName())
                .statementChecksum(successMultipartResponse().getBytes())
                .statementFileName(COMMON_DOC_NAME)
                .userId(COMMON_USER_ID)
                .isDeleted(false)
                .fileUploadStatus(UploadStatusEnum.UPLOADED)
                .documentId(1L)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();
    }

    public static AccountStatementDto getUploadedAccountStatementDto() throws IOException {
        return AccountStatementDto.builder()
                .s3StatementKey(getFileName())
                .statementChecksum(successMultipartResponse().getBytes())
                .statementFileName(COMMON_DOC_NAME)
                .userId(COMMON_USER_ID)
                .isDeleted(false)
                .fileUploadStatus(UploadStatusEnum.UPLOADED)
                .build();
    }

    public static DocumentResponse expectdDocumentResponse() {
        return DocumentResponse.builder()
                .fileName(COMMON_DOC_NAME)
                .documentId(COMMON_DOC_ID)
                .build();

    }

    public static byte[] getCheckSum() {
        return new byte[]{
                -71, -113, -64, -102, -64, -33, 59, -68, 30, -27, -25, -109,
                22, 96, 79, 116, 98, -1, -3, -16, -107, -63, -58, 118,
                -29, -62, 81, 119, 115, 100, 95, -23
        };

    }


    public static String getFileName() {
        var accountUploadDate = LocalDate.now();
        return COMMON_USER_ID + "-" + "account-statement" + "-" + accountUploadDate.getMonth() + "_" + accountUploadDate.getYear();
    }

    public static MockMultipartFile successMultipartResponse() {

        return new MockMultipartFile("file", COMMON_DOC_NAME,
                MediaType.APPLICATION_PDF_VALUE, TEST_CONTENT.getBytes());
    }


}
