package com.test.statementservice.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class DocumentException extends RuntimeException {

    private final HttpStatus status;

    public DocumentException(final HttpStatus httpStatus, final String errorMessage) {
        super(errorMessage);
        status = httpStatus;

    }
}
