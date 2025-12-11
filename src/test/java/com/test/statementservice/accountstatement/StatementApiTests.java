package com.test.statementservice.accountstatement;

import com.test.statementservice.config.StatementConfigTest;
import com.test.statementservice.controller.StatementController;
import com.test.statementservice.service.StatementService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = StatementController.class)
@Import(StatementConfigTest.class)
@ActiveProfiles("test")
class StatementApiTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatementService mockedStatementService;


    @Test
    void uploadDocumentTest() throws Exception {
        var testFile = StatementApiTestUtils.successMultipartResponse();
        Mockito.when(mockedStatementService.uploadAccountStatement(StatementApiTestUtils.COMMON_USER_ID, testFile))
                .thenReturn(StatementApiTestUtils.getSuccessDocumentResponse());

        mockMvc.perform(multipart("/statement/v1/upload-statement")
                        .file(testFile)
                        .param("statementOwner", StatementApiTestUtils.COMMON_USER_ID))
                .andExpect(status().isOk());
    }
}
