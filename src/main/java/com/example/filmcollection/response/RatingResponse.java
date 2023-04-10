package com.example.filmcollection.response;

import com.example.filmcollection.entity.Films;
import com.example.filmcollection.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RatingResponse {
    private String message;
    private String user;
    private String film;
    private int totalFilmsRated;
    private LocalDateTime ratedAt;

    public RatingResponse(String message, String user, String film, int totalFilmsRated, LocalDateTime ratedAt) {
        this.message = message;
        this.user = user;
        this.film = film;
        this.totalFilmsRated = totalFilmsRated;
        this.ratedAt = ratedAt;
    }

    public RatingResponse(String message) {
        this.message = message;
    }
}
