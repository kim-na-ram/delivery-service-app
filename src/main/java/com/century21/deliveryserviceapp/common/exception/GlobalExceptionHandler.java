package com.century21.deliveryserviceapp.common.exception;

import com.century21.deliveryserviceapp.common.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<?> handleApiException(ApiException apiException) {
        ErrorResponse errorResponse = ErrorResponse.from(apiException);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(apiException.getCode()));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> handleException(Exception exception) {
        HttpStatus code = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse errorResponse = ErrorResponse.of(code.value(), exception.getMessage());
        return new ResponseEntity<>(errorResponse, code);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        HttpStatus code = HttpStatus.BAD_REQUEST;

        StringBuilder sb = new StringBuilder();
        exception.getFieldErrors()
                .forEach(fieldError ->
                        sb.append(fieldError.getField())
                                .append(" : ")
                                .append(fieldError.getDefaultMessage())
                );
        ErrorResponse errorResponse = ErrorResponse.of(code.value(), sb.toString());
        return new ResponseEntity<>(errorResponse, code);
    }
}