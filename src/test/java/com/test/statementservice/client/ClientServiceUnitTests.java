package com.test.statementservice.client;

import com.test.statementservice.StatementApiApplication;
import com.test.statementservice.persistance.repository.AccountStatementRepository;
import com.test.statementservice.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest(classes = StatementApiApplication.class)
@ActiveProfiles("test")
class ClientServiceUnitTests {

    @MockBean
    private AccountStatementRepository mockedRepository;

    @Autowired
    private ClientService clientService;


}
