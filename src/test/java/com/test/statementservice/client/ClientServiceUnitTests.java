package com.test.statementservice.client;

import com.test.statementservice.StatementApiApplication;
import com.test.statementservice.enums.UploadStatusEnum;
import com.test.statementservice.mapper.StatementMapper;
import com.test.statementservice.persistance.repository.AccountStatementRepository;
import com.test.statementservice.s3integration.service.S3IntegrationService;
import com.test.statementservice.service.ClientService;
import com.test.statementservice.web.UserStore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.test.statementservice.accountstatement.StatementApiTestUtils.COMMON_USER_ID;


@SpringBootTest(classes = StatementApiApplication.class)
@ActiveProfiles("test")
class ClientServiceUnitTests {

    @MockBean
    private AccountStatementRepository mockedRepository;

    @MockBean
    private UserStore mockedUserStore;

    @MockBean
    private S3IntegrationService mockedS3IntegrationService;

    @MockBean
    private StatementMapper mockedStatementMapper;

    @Autowired
    private ClientService clientService;

    Duration duration = Duration.ofHours(1);


    @BeforeEach
    void setUp() throws IOException {
        ReflectionTestUtils.setField(clientService, "expirationDate", duration);
        Mockito.when(mockedUserStore.getUserId()).thenReturn(COMMON_USER_ID);
    }

    @Test
    void successSignedUrlGenerationAccountStatementTest() throws IOException {
        var expectedResult = ClientApiTestUtils.expectedSignedStatementResponse();
        Mockito.when(mockedRepository
                        .findByDocumentIdAndFileUploadStatusAndIsDeleted(ClientApiTestUtils.COMMON_DOC_ID, UploadStatusEnum.UPLOADED, false))
                .thenReturn(Optional.of(ClientApiTestUtils.getUploadedAccountStatementEntity()));
        Mockito.when(mockedS3IntegrationService.
                generateS3SignedUrl(ClientApiTestUtils.getFileName(), duration)).thenReturn(ClientApiTestUtils.COMMON_URL);
        var resultResponse = clientService.generateAccountStatementPDF(ClientApiTestUtils.COMMON_DOC_ID);
        Assertions.assertNotNull(resultResponse);
        Assertions.assertNotNull(resultResponse.getDocumentId());
        Assertions.assertEquals(expectedResult.getDocumentId(), resultResponse.getDocumentId());
    }

    @Test
    void successRetrievalOfAccountStatementTest() throws IOException {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusDays(1);
        Mockito.when(mockedRepository
                        .findByStatementsForSpecificDuration(ClientApiTestUtils.COMMON_USER_ID, UploadStatusEnum.UPLOADED, startTime, endTime))
                .thenReturn(Optional.of(ClientApiTestUtils.getListOfUploadedAccountStatementEntity()));
        Mockito.when(mockedS3IntegrationService.
                generateS3SignedUrl(ClientApiTestUtils.getFileName(), duration)).thenReturn(ClientApiTestUtils.COMMON_URL);
        var resultResponse = clientService.getAccountStatements(startTime, endTime);
        Assertions.assertNotNull(resultResponse);
        Assertions.assertNotNull(!resultResponse.getDocuments().isEmpty());
    }


}
