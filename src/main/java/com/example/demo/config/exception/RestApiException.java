package com.example.demo.config.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Getter
@Setter
@NoArgsConstructor
public class RestApiException {
    private String errorMessage;
    private HttpStatus httpStatus;


    public RestApiException(HttpStatus methodNotAllowed, String s) {
        this.errorMessage = s;
        this.httpStatus = methodNotAllowed;
    }
}


