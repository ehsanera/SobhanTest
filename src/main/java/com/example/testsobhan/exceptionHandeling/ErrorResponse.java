package com.example.testsobhan.exceptionHandeling;

import org.springframework.http.HttpStatus;

public interface ErrorResponse {

    String getKey();

    String getMessage();

    HttpStatus getHttpStatus();
}