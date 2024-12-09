package com.dataquadinc.exceptions;

import com.dataquadinc.dto.ErrorResponseBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseBean> handleValidationException(ValidationException ex) {



        Map<String, String> errors = ex.getErrors();  // Get the validation errors

        // Construct the error response
        ErrorResponseBean errorResponse = ErrorResponseBean.builder()
                .success(false)  // Indicate failure
                .message("unsuccessfull")
                .error("Validation Error")  // Set the error message
                .fieldErrors(errors)  // Set the validation errors in the fieldErrors map
                .build();
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            errors.put(fieldName, "Invalid value for " + fieldName + ". Please check and provide the correct information.");
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
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
