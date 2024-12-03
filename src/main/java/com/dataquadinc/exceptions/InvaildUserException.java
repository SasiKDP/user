package com.dataquadinc.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;


public class InvaildUserException extends RuntimeException {

    public InvaildUserException() {
        super();

    }

    public InvaildUserException(String message) {
        super(message);

    }

}

