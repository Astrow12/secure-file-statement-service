package com.test.statementservice.accountstatement;

import com.test.statementservice.StatementApiApplication;
import com.test.statementservice.mapper.StatementMapper;
import com.test.statementservice.persistance.repository.AccountStatementRepository;
import com.test.statementservice.s3integration.service.S3IntegrationService;
import com.test.statementservice.service.StatementService;
import com.test.statementservice.web.UserStore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.Optional;

import static com.test.statementservice.accountstatement.StatementApiTestUtils.COMMON_USER_ID;
import static org.mockito.ArgumentMatchers.any;


@SpringBootTest(classes = StatementApiApplication.class)
@ActiveProfiles("test")
class StatementServiceUnitTests {

    @MockBean
    private AccountStatementRepository mockedRepository;

    @MockBean
    private UserStore mockedUserStore;

    @MockBean
    private S3IntegrationService mockedS3IntegrationService;

    @MockBean
    private StatementMapper mockedStatementMapper;

    @Autowired
    private StatementService statementService;

    @BeforeEach
    void setUp() throws IOException {
        Mockito.when(mockedUserStore.getUserId()).thenReturn(COMMON_USER_ID);
        Mockito.doNothing().when(mockedS3IntegrationService).uploadPdfToS3(StatementApiTestUtils.getFileName(), StatementApiTestUtils.successMultipartResponse().getBytes());
    }

    @Test
    void successUploadAccountStatementTest() throws IOException {
        var testFile = StatementApiTestUtils.successMultipartResponse();
        var expectedResult = StatementApiTestUtils.expectdDocumentResponse();
        Mockito.when(mockedRepository.findByStatementChecksumAndIsDeleted(StatementApiTestUtils.getCheckSum(), false))
                .thenReturn(Optional.empty());
        Mockito.when(mockedStatementMapper.createAccountStatementDto(any(), any(), any(), any(), any()))
                .thenReturn(StatementApiTestUtils.getUploadedAccountStatementDto());
        Mockito.when(mockedStatementMapper.convertToStatementEntity(any()))
                .thenReturn(StatementApiTestUtils.getUploadedAccountStatementEntity());
        Mockito.when(mockedRepository.save(any()))
                .thenReturn(StatementApiTestUtils.getUploadedAccountStatementEntity());
        var resultResponse = statementService.uploadAccountStatement(COMMON_USER_ID, testFile);
        Assertions.assertNotNull(resultResponse);
        Assertions.assertNotNull(resultResponse.getDocumentId());
        Assertions.assertEquals(expectedResult.getDocumentId(), resultResponse.getDocumentId());


    }


}
