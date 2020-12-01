package com.ishapirov.telegrambot.exceptionhandling;

import com.ishapirov.telegrambot.exceptionhandling.exceptionresponse.ExceptionResponse;
import com.ishapirov.telegrambot.exceptionhandling.exceptions.UnexpectedInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnexpectedInputException.class)
    public ResponseEntity<ExceptionResponse> badRequest(UnexpectedInputException unexpectedInputException){
        ExceptionResponse response = new ExceptionResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(500);
        response.setError("INTERNAL_SERVER_ERROR");
        response.setMessage(unexpectedInputException.getMessage());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
