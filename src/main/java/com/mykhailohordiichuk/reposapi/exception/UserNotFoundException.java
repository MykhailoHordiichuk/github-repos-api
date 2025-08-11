package com.mykhailohordiichuk.reposapi.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("GitHub user not found: " + username);
    }
}