package com.JobPortal.JPP.exceptions;

public class JobAccessDeniedException extends RuntimeException {
    public JobAccessDeniedException(String message) {
        super(message);
    }
}
