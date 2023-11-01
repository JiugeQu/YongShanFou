package com.mizore.mob.exception;

public class SseException extends RuntimeException{

    public SseException(String message) {
        super(message);
    }

    public SseException(String message, Exception e) {
        super(message, e);
    }
}
