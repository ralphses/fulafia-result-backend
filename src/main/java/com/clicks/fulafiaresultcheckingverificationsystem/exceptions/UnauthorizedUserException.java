package com.clicks.fulafiaresultcheckingverificationsystem.exceptions;

public class UnauthorizedUserException extends RuntimeException{

    public UnauthorizedUserException(String message) {
        super(message);
    }

    public UnauthorizedUserException() {
        super();
    }
}
