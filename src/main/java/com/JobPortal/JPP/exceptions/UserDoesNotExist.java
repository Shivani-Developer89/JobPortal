package com.JobPortal.JPP.exceptions;

public class UserDoesNotExist extends RuntimeException {
    public UserDoesNotExist(){
        super("User Doesn't exist.");

    }
    public UserDoesNotExist(String m){
        super(m);
    }

}
