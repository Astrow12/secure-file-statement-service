package com.test.statementservice.s3integration.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class S3IntegrationException extends RuntimeException {

    private final HttpStatus status;

    public S3IntegrationException(final HttpStatus httpStatus, final String errorMessage) {
        super(errorMessage);
        status = httpStatus;

    }
}
