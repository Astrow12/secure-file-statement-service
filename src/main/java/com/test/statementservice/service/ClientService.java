package com.test.statementservice.service;

import com.test.statementservice.model.response.SignedStatementResponse;
import com.test.statementservice.model.response.StatementsResponse;

import java.time.LocalDateTime;

public interface ClientService {

    SignedStatementResponse generateAccountStatementPDF(Long documentId);

    StatementsResponse getAccountStatements(LocalDateTime startDate, LocalDateTime endDate);
}
