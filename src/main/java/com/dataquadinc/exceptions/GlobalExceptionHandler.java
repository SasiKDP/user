package com.dataquadinc.exceptions;

import com.dataquadinc.dto.ErrorResponseBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseBean> handleValidationException(ValidationException ex) {


       // log.error("Validation Error: {}", ex.getErrors());
        ErrorResponseBean errorResponse = ErrorResponseBean.builder().status(HttpStatus.BAD_REQUEST.value()).error("Validation Error").fieldErrors(ex.getErrors()).build();

        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvaildUserException.class)
    public ResponseEntity<Map<String, String>> handleInvaildUserException(InvaildUserException ex) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", ex.getMessage());
        // Response<Map<String, String>> response = new Response<>(map,
        // HttpStatus.NOT_FOUND.name());
        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }


}
