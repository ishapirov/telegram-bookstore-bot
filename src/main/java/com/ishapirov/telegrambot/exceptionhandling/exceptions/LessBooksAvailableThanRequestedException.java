package com.ishapirov.telegrambot.exceptionhandling.exceptions;

public class LessBooksAvailableThanRequestedException extends RuntimeException {
    public LessBooksAvailableThanRequestedException() {
        super();
    }

    public LessBooksAvailableThanRequestedException(String message) {
        super(message);
    }
}
