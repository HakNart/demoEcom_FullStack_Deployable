package com.kt.rest.demoEcommerce.controllers.exeptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
