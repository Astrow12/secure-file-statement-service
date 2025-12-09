package com.test.statementservice.controller;


import com.test.statementservice.exception.DocumentException;
import com.test.statementservice.model.response.DocumentResponse;
import com.test.statementservice.service.StatementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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

    @Value("${statement.max-file-size}")
    private Long allowedFileSize;


    @PostMapping(path = "/upload-statement", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Upload pdf statement for user")
    public ResponseEntity<DocumentResponse> uploadAccountStatement(final @RequestParam(value = "file", required = true) MultipartFile accountStatement,
                                                                   final @Valid @NotNull(message = "statementOwner cannot be null")
                                                                   @NotBlank(message = "statementOwner cannot be empty") @RequestParam(value = "statementOwner", required = true)
                                                                   String fileOwner) {
        isValid(accountStatement, allowedFileSize);
        log.trace("uploadAccountStatement");
        return ResponseEntity.ok(statementService.uploadAccountStatement(fileOwner, accountStatement));
    }

    private void isValid(MultipartFile fileToUpload, Long allowedSize) {
        log.info("Validating file for upload");
        if (fileToUpload == null || fileToUpload.isEmpty() ||
                !fileToUpload.getContentType().equalsIgnoreCase((MediaType.APPLICATION_PDF_VALUE))) {
            log.error("File for upload has failed validations");
            throw new DocumentException(HttpStatus.BAD_REQUEST, "Invalid file for upload");

        }
    }


}
