package com.hampcode.bankingservice.exceptions;

public class ResourceNotFoundException extends  RuntimeException {

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
