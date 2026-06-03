package com.JobPortal.JPP.exceptions;

import com.JobPortal.JPP.dto.response.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserDoesNotExist.class)
    public ResponseEntity<ErrorResponseDTO> handleUserDoesNotExist(
            UserDoesNotExist e) {

        ErrorResponseDTO errorResponseDTO =
                new ErrorResponseDTO(
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        e.getMessage(),
                        "User doesn't exist!"
                );

        return new ResponseEntity<>(
                errorResponseDTO,
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidCredentials(
            InvalidCredentialsException e) {


        ErrorResponseDTO errorResponseDTO =
                new ErrorResponseDTO(
                        LocalDateTime.now(),
                        HttpStatus.UNAUTHORIZED.value(),
                        e.getMessage(),
                        "Authentication Failed"
                );

        return new ResponseEntity<>(
                errorResponseDTO,
                HttpStatus.UNAUTHORIZED
        );
    }
    @ExceptionHandler(AlreadyAppliedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAlreadyAppliedException(
            AlreadyAppliedException ex) {

        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                "Application already exists"
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDenied(
            AccessDeniedException ex) {

        ErrorResponseDTO error =
                new ErrorResponseDTO(        LocalDateTime.now(),
                        HttpStatus.FORBIDDEN.value(),
                        ex.getMessage(),
                        "Access denied"
                );

        error.setMessage(ex.getMessage());

        return new ResponseEntity<>(
                error,
                HttpStatus.FORBIDDEN);
    }
}