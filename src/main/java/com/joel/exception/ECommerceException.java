package com.joel.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ECommerceException {
    private final Integer httpCode;
    private final HttpStatus httpStatus;
    private final String message;
    private final String description;

}
