package com.samy.service.cognitoapp.exception;

public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = 1862618285700942977L;
    public BadRequestException(String msg) {
        super(msg);
    }
}
