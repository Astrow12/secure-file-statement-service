package com.test.statementservice.exception;

import org.springframework.http.HttpStatus;

public class JwtMissingException extends RuntimeException {

    private final HttpStatus status;

    public JwtMissingException(final HttpStatus httpStatus, final String errorMessage) {
        super(errorMessage);
        status = httpStatus;

    }
}
