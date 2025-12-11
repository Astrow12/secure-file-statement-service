package com.test.statementservice.client;


import com.test.statementservice.enums.UploadStatusEnum;
import com.test.statementservice.model.dto.DocumentDto;
import com.test.statementservice.model.request.ClientDataRequest;
import com.test.statementservice.model.response.SignedStatementResponse;
import com.test.statementservice.model.response.StatementsResponse;
import com.test.statementservice.persistance.entity.AccountStatementEntity;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public final class ClientApiTestUtils {

    public static final Long COMMON_DOC_ID = 1L;
    public static final String COMMON_DOC_NAME = "Test file";
    public static final String TEST_CONTENT = "TestContent";
    public static final String COMMON_USER_ID = "TshepoM";


    public static final String COMMON_URL = "signed/url";


    public static ClientDataRequest getClientDataRequest() {
        return ClientDataRequest.builder()
                .documentId(COMMON_DOC_ID)
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
                .documentId(COMMON_DOC_ID)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();
    }

    public static List<AccountStatementEntity> getListOfUploadedAccountStatementEntity() throws IOException {
        return List.of(getUploadedAccountStatementEntity());
    }


    public static SignedStatementResponse expectedSignedStatementResponse() {
        return SignedStatementResponse.builder()
                .signedAccountStatement(COMMON_URL)
                .documentId(COMMON_DOC_ID)
                .build();

    }

    public static StatementsResponse expectedStatementsResponse() {
        return StatementsResponse.builder()
                .documents(getDocuments())
                .build();

    }

    public static List<DocumentDto> getDocuments() {
        return List.of(DocumentDto.builder()
                .documentId(COMMON_DOC_ID)
                .fileName(COMMON_DOC_NAME)
                .build());
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
