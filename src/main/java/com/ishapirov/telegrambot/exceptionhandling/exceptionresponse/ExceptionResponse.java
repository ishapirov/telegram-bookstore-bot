package com.ishapirov.telegrambot.exceptionhandling.exceptionresponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ExceptionResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private String message;
    private List<Violation> inputViolations = new ArrayList<>();
}

