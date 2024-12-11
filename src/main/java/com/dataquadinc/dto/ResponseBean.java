package com.dataquadinc.dto;

import lombok.Data;

@Data
public class ResponseBean<T> {

    private boolean success;
    private String message;
    private T data;
    private String error;




}
