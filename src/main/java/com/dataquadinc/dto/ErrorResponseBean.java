package com.dataquadinc.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class  ErrorResponseBean<T> {
    public boolean success;
    public String message;
    private T data;
    private Map<String,String> error;
}
