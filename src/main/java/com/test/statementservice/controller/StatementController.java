package com.test.statementservice.controller;


import com.test.statementservice.model.response.MessageResponse;
import com.test.statementservice.service.StatementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/statement/v1")
@Tag(name = "Internal Account Statement API", description = "Operations related to CRUD for account statements")
public class StatementController {

    private final StatementService statementService;


    @PostMapping(path = "/upload-statement", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Upload pdf statement for user")
    public ResponseEntity<MessageResponse> uploadAccountStatement(final @Valid @RequestParam("file") MultipartFile accountStatement,
                                                                  final @Valid @NotNull @NotBlank @RequestParam("statementOwner")
                                                                  String fileOwner) {
        log.trace("uploadAccountStatement");
        return ResponseEntity.ok(statementService.uploadAccountStatement(fileOwner, accountStatement));
    }
}
