package com.ishapirov.telegrambot.exceptionhandling.exceptions;

public class UnexpectedInputException extends RuntimeException{
    public UnexpectedInputException(String message){
        super(message);
    }
}
