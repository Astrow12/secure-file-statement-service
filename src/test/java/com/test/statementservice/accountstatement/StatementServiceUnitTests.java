package com.test.statementservice.accountstatement;

import com.test.statementservice.StatementApiApplication;
import com.test.statementservice.model.response.DocumentResponse;
import com.test.statementservice.persistance.entity.AccountStatementEntity;
import com.test.statementservice.persistance.repository.AccountStatementRepository;
import com.test.statementservice.service.StatementService;
import com.test.statementservice.web.UserStore;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;


@SpringBootTest(classes = StatementApiApplication.class)
@ActiveProfiles("test")
class StatementServiceUnitTests {

	@MockBean
	private AccountStatementRepository mockedRepository;

	@MockBean
	private UserStore mockedUserStore;

	@MockBean
	private EntityManager mockedEntityManager;

	@Autowired
	private StatementService statementService;

	@BeforeEach
	void setUp() {
		Mockito.when(mockedUserStore.getUserName()).thenReturn("TestUser");
	}
}
