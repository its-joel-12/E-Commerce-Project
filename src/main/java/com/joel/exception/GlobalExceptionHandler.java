package com.joel.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Invalid field data
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ECommerceException> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> res = new HashMap<>();
        ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors().forEach((er) -> {
            String fieldName = ((FieldError) er).getField();
            String message = er.getDefaultMessage();
            res.put(fieldName, message);
        });

        ECommerceException error = new ECommerceException(
                400,
                HttpStatus.BAD_REQUEST,
                "Invalid Inputs !",
                res.toString()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ECommerceException> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ECommerceException error = new ECommerceException(
                404,
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                "Required Resource Not Found, hence the operation was unsuccessful!!"
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // ApiException
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ECommerceException> handleApiException(ApiException ex) {
        ECommerceException error = new ECommerceException(
                400,
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                "Bad Request!!"
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
