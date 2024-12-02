package com.dataquadinc.dto;

import lombok.Data;

@Data
public class ResponseBean<T> {

    private int status;
    private String message;
    private T data;
}
