package com.example.demo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
    private boolean success;
    private T data;

    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(true, data);
    }

}
