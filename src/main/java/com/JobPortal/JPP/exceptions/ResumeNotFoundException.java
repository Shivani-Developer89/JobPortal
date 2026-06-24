package com.JobPortal.JPP.exceptions;

public class ResumeNotFoundException
        extends RuntimeException {

    public ResumeNotFoundException(
            String message) {
        super(message);
    }
}
