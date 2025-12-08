package com.test.statementservice.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.statementservice.StatementApiApplication;
import com.test.statementservice.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@Slf4j
@SpringBootTest(classes = StatementApiApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class ClientApiTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService mockedClientService;

    @Autowired
    ObjectMapper objectMapper;
}
