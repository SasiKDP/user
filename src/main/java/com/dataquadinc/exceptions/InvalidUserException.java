package com.dataquadinc.exceptions;

public class InvalidUserException extends RuntimeException {

    // Constructor that accepts a message and passes it to the parent RuntimeException
    public InvalidUserException(String message) {
        super(message);
    }
}
