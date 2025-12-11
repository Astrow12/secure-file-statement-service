package com.test.statementservice.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.statementservice.config.StatementConfigTest;
import com.test.statementservice.controller.ClientController;
import com.test.statementservice.model.response.SignedStatementResponse;
import com.test.statementservice.model.response.StatementsResponse;
import com.test.statementservice.service.ClientService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ClientController.class)
@Import(StatementConfigTest.class)
@ActiveProfiles("test")
class ClientApiTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService mockedClientService;

    @Autowired
    ObjectMapper objectMapper;

    private String hostUrl = "/client/v1";

    @Test
    void successfulRetrievalOfAccDocsTest() throws Exception {
        ResponseEntity<StatementsResponse> response = ResponseEntity.ok(ClientApiTestUtils.expectedStatementsResponse());
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusDays(1);
        Mockito.when(mockedClientService.getAccountStatements(startTime, endTime)).thenReturn(response.getBody());
        mockMvc.perform(get(hostUrl + "/account-statement/list-by-duration")
                        .param("startDate", startTime.format(DateTimeFormatter.ISO_DATE_TIME))
                        .param("endDate", endTime.format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    var resultResponse = result.getResponse().getContentAsString();
                    Assertions.assertNotNull(resultResponse);
                });
    }

    @Test
    void successfulRetrievalOfAccountStatementTest() throws Exception {
        ResponseEntity<SignedStatementResponse> response = ResponseEntity.ok(ClientApiTestUtils.expectedSignedStatementResponse());
        Mockito.when(mockedClientService.generateAccountStatementPDF(ClientApiTestUtils.COMMON_DOC_ID)).thenReturn(response.getBody());
        mockMvc.perform(post(hostUrl + "/account-statement/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ClientApiTestUtils.getClientDataRequest())))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    var resultResponse = result.getResponse().getContentAsString();
                    Assertions.assertNotNull(resultResponse);
                });
    }

}
