package com.dataquadinc.exceptions;

import com.dataquadinc.dto.ErrorResponseBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseBean> handleValidationException(ValidationException ex) {


       // log.error("Validation Error: {}", ex.getErrors());
        ErrorResponseBean errorResponse = ErrorResponseBean.builder().status(HttpStatus.BAD_REQUEST.value()).error("Validation Error").fieldErrors(ex.getErrors()).build();

        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

}
