package com.dataquadinc.dto;

import java.util.Map;

public class ErrorResponseBean<T> {
    private boolean success;
    private String message;
    private T data;
    private Map<String, String> error;

    private ErrorResponseBean(Builder<T> builder) {
        this.success = builder.success;
        this.message = builder.message;
        this.data = builder.data;
        this.error = builder.error;
    }

    public static class Builder<T> {
        private boolean success;
        private String message;
        private T data;
        private Map<String, String> error;

        public Builder<T> success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Builder<T> error(Map<String, String> error) {
            this.error = error;
            return this;
        }

        public ErrorResponseBean<T> build() {
            return new ErrorResponseBean<>(this);
        }
    }

    // Getters and Setters (can be generated if needed)
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

    public Map<String, String> getError() {
        return error;
    }

    public void setError(Map<String, String> error) {
        this.error = error;
    }
}
