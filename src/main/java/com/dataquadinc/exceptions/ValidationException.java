package com.dataquadinc.exceptions;

import java.util.Map;

public class ValidationException extends RuntimeException {
    private Map<String, String> errors;  // To store the validation errors

    // Constructor that accepts a map of errors
    public ValidationException(Map<String, String> errors) {
        super("Validation failed");
        this.errors = errors;
    }

    // Getter for errors
    public Map<String, String> getErrors() {
        return errors;
    }
}
