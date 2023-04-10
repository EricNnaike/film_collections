package com.example.filmcollection.exceptions;

public class JwtInvalidException extends RuntimeException {
    public JwtInvalidException(String message) {
        super(message);
    }
}
