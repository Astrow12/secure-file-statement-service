package com.test.statementservice.controller;


import com.test.statementservice.model.request.ClientDataRequest;
import com.test.statementservice.model.response.SignedStatementResponse;
import com.test.statementservice.model.response.StatementsResponse;
import com.test.statementservice.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/client/v1")
@Tag(name = "External Account Statement API", description = "Operations related to retrieve customer account statements")
public class ClientController {

    private final ClientService clientService;

    @GetMapping(path = "/account-statement/list-by-duration", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatementsResponse> getAccountStatementDocIds(final @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.trace("getAccountStatementDocIds");
        return ResponseEntity.ok(clientService.getAccountStatements(startDate, endDate));
    }

    @PostMapping(path = "/account-statement/generate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retrieve account statement for user")
    public ResponseEntity<SignedStatementResponse> getAccountStatement(final @Valid @RequestBody ClientDataRequest clientDataRequest) {
        log.trace("getAccountStatement");
        return ResponseEntity.ok(clientService.generateAccountStatementPDF(clientDataRequest.getDocumentId()));
    }


}
