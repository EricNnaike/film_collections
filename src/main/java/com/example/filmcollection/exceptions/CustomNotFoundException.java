package com.example.filmcollection.exceptions;

public class CustomNotFoundException extends RuntimeException{
    public CustomNotFoundException(String message) {
        super(message);
    }
}
