package com.kt.rest.demoEcommerce.exeptions;

import com.kt.rest.demoEcommerce.model.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class ExceptionHandleInterceptor {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handle(IllegalArgumentException e) {
        log.info(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponse.builder().fail(e.getMessage()).build()
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handle(ResourceNotFoundException e) {
        log.info(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.builder().fail(e.getMessage()).build()
        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse> handle(NoSuchElementException e) {
        log.info(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.builder().fail(e.getMessage()).build()
        );
    }
    // DataIntegrityViolationException.class

    // Catch all exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handle(Exception e) {
        log.info("Unknown error has occurred: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.builder().error("Unknown error has occurred.").build()
        );
    }
}
