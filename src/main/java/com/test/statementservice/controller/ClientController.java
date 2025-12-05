package com.test.statementservice.controller;


import com.test.statementservice.model.request.ClientDataRequest;
import com.test.statementservice.model.response.ClientDataResponse;
import com.test.statementservice.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/client/v1")
@Tag(name = "External Account Statement API", description = "Operations related to retrieve customer account statements")
public class ClientController {

    private final ClientService clientService;


}
