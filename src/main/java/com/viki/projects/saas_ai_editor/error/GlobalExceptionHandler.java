package com.viki.projects.saas_ai_editor.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException e) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, e.getMessage());
        log.error(apiError.toString(), e);
        return ResponseEntity.status(apiError.status()).body(apiError);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException e) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, e.getResourceName() + " with ID " + e.getResourceId() + " not found");
        log.error(apiError.toString(), e);
        return ResponseEntity.status(apiError.status()).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        List<ApiFieldError> errors = e.getBindingResult().getFieldErrors().stream().
                map(fieldError -> new ApiFieldError(fieldError.getField(), fieldError.getDefaultMessage())).
                toList();

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Validation failed for one or more fields", errors);
        log.error(apiError.toString(), e);
        return ResponseEntity.status(apiError.status()).body(apiError);
    }
}
