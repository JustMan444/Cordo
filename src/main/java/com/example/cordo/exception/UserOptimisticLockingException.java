package com.example.cordo.exception;

public class UserOptimisticLockingException extends RuntimeException {
    public UserOptimisticLockingException(String message) {
        super(message);
    }
}
