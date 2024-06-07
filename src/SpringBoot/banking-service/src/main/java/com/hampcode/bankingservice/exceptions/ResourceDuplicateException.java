package com.hampcode.bankingservice.exceptions;

public class ResourceDuplicateException extends RuntimeException{
    public ResourceDuplicateException() {
    }

    public ResourceDuplicateException(String message) {
        super(message);
    }
}
