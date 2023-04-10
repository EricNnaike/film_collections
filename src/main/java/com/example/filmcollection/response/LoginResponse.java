package com.example.filmcollection.response;

import lombok.Data;
@Data
public class LoginResponse {
    private String message;
    private Long id;
    private String email;

    public LoginResponse(String message, Long id, String email) {
        this.message = message;
        this.id = id;
        this.email = email;
    }

    public LoginResponse(String message) {
        this.message = message;
    }

    public LoginResponse() {
    }
}
