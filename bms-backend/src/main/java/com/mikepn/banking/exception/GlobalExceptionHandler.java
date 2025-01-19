package com.mikepn.banking.exception;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.mikepn.banking.domains.ApiResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableWebMvc
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }



    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, Locale locale) {
        String errorMessage = messageSource.getMessage(ex.getMessage(), ex.getArgs(), locale);
        return new ApiResponse<>(null,errorMessage, HttpStatus.NOT_FOUND).toResponseEntity();
    }


    @ExceptionHandler(DuplicateRecordException.class)
    public ResponseEntity<?> duplicateRecordException(DuplicateRecordException ex , Locale locale){
        String errorMessage = messageSource.getMessage(ex.getMessage(), ex.getArgs(), locale);
        return new ApiResponse<>(null, errorMessage, HttpStatus.BAD_REQUEST).toResponseEntity();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex, Locale locale) throws JsonProcessingException {
        String message = ex.getMessage();
        String errorMessage = messageSource.getMessage("exceptions.global",null, locale);
        return new ApiResponse<>(message,errorMessage, HttpStatus.INTERNAL_SERVER_ERROR).toResponseEntity();
    }


    /**
     * Handle validation errors (e.g., `@Valid` or `@Validated` annotations).
     */


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public  ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex
    ){
        Map<String , String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ApiResponse<Map<String, String>> response = new ApiResponse<>(
                errors,
                "Validation Failed",
                HttpStatus.BAD_REQUEST
        );

        return response.toResponseEntity();
    }




}
