package com.century21.deliveryserviceapp.common.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private int code;
    private String message;

    private ApiException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ApiException of(ResponseCode responseCode) {
        return new ApiException(responseCode.getStatus().value(), responseCode.getMessage());
    }
}
