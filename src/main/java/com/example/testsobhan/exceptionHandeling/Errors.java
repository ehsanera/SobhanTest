package com.example.testsobhan.exceptionHandeling;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum Errors implements ErrorResponse {
    PRODUCT_NOT_FOUND("PRODUCT_NOT_FOUND", HttpStatus.NOT_FOUND, "Product with id {id} not found"),
    PRODUCT_FOUND("PRODUCT_FOUND", HttpStatus.CONFLICT, "Product with name {name} found");
    String key;
    HttpStatus httpStatus;
    String message;

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
