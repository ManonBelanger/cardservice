package com.example.cardservice.exceptions;

public class ConcurrentException extends Exception {
    public ConcurrentException(String message) {
        super(message);
    }
}
