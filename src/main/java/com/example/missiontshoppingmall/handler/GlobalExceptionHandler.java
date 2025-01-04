package com.example.missiontshoppingmall.handler;

import com.example.missiontshoppingmall.exception.InvalidBusinessAccountException;
import com.example.missiontshoppingmall.exception.InvalidProfilePictureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidProfilePictureException.class)
    public ResponseEntity<String> handleInvalidProfilePictureException(InvalidProfilePictureException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidBusinessAccountException.class)
    public ResponseEntity<String> handleInvalidBusinessException(InvalidBusinessAccountException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
