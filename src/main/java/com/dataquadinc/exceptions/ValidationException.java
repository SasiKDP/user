package com.dataquadinc.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends RuntimeException{
    private final Map<String, String> errors;

    public ValidationException (
            Map<String, String> errors
    ) {
        super("Validation Failed");
        this.errors = errors;
    }

    public Map<String,String> getErrors() {
        return errors;
    }
}
