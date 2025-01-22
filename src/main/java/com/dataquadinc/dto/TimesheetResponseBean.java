package com.dataquadinc.dto;

public class TimesheetResponseBean<T> {

    private boolean success;
    private String message;
    private T data;  // This is where your data will go
    private ErrorDetail error;  // Error details

    // Constructor
    public TimesheetResponseBean() {}

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ErrorDetail getError() {
        return error;
    }

    public void setError(ErrorDetail error) {
        this.error = error;
    }

    // Nested ErrorDetail class
    public static class ErrorDetail {
        private String errormessage;
        private String errorcode;

        // Constructor
        public ErrorDetail(String errormessage, String errorcode) {
            this.errormessage = errormessage;
            this.errorcode = errorcode;
        }

        // Getters and Setters
        public String getErrormessage() {
            return errormessage;
        }

        public void setErrormessage(String errormessage) {
            this.errormessage = errormessage;
        }

        public String getErrorcode() {
            return errorcode;
        }

        public void setErrorcode(String errorcode) {
            this.errorcode = errorcode;
        }
    }
}