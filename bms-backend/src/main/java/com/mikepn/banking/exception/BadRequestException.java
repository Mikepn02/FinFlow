package com.mikepn.banking.exception;


import lombok.Getter;

import java.io.Serial;

@Getter
public class BadRequestException extends RuntimeException{

    @Serial
    public static final long serialVersionUID = 1L;

    public BadRequestException(String message) {
        super(message);
    }


    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
