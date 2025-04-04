package com.civia.mandate.controller.advise;

import com.civia.mandate.exception.HistorySaveException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HistorySaveException.class)
    public ResponseEntity<String> handleValidationException(HistorySaveException ex) {

        return new ResponseEntity<>(ex.getMessages(), HttpStatus.BAD_REQUEST);
    }
}