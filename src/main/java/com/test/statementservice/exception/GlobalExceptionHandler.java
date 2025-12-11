package com.test.statementservice.exception;


import com.test.statementservice.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({jakarta.validation.ConstraintViolationException.class,
            NumberFormatException.class,
            HttpMessageNotReadableException.class})
    protected ResponseEntity<Object> handleConflict(final RuntimeException ex, final WebRequest request) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .date(OffsetDateTime.now())
                        .message(ex.getMessage())
                        .build(),
                HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({JwtMissingException.class})
    protected ResponseEntity<Object> handleJwtMissingException(final JwtMissingException ex, final WebRequest request) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .date(OffsetDateTime.now())
                        .message(ex.getMessage())
                        .build(),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({DocumentException.class})
    protected ResponseEntity<Object> handleDocumentException(final DocumentException ex, final WebRequest request) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .date(OffsetDateTime.now())
                        .message(ex.getMessage())
                        .build(),
                ex.getStatus());
    }

    @ExceptionHandler({ClientException.class})
    protected ResponseEntity<Object> handleClientExceptions(final ClientException ex, final WebRequest request) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .date(OffsetDateTime.now())
                        .message(ex.getMessage())
                        .build(),
                ex.getStatus());
    }


    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<Object> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex, final WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        List<String> errorMessages = bindingResult.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .date(OffsetDateTime.now())
                        .message(errorMessages.stream().collect(Collectors.joining(",")))
                        .build(),
                HttpStatus.BAD_REQUEST);
    }


}
