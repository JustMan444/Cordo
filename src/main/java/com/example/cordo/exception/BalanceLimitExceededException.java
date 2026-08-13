package com.example.cordo.exception;

public class BalanceLimitExceededException extends RuntimeException {
    public BalanceLimitExceededException(String message) {
        super(message);
    }
}
