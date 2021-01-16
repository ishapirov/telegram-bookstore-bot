package com.ishapirov.telegrambot.exceptionhandling.exceptions;

public class BookAlreadyInCartException extends RuntimeException{
    public BookAlreadyInCartException() {
        super();
    }

    public BookAlreadyInCartException(String message) {
        super(message);
    }
}
