package com.example.filmcollection.service;

import com.example.filmcollection.dto.LoginDTO;
import com.example.filmcollection.dto.RateDTO;
import com.example.filmcollection.dto.UserDTO;
import com.example.filmcollection.entity.Films;
import com.example.filmcollection.entity.Users;
import com.example.filmcollection.response.*;

import java.util.List;

public interface UserService {
    Users create(UserDTO userDTO);
    LoginResponse login(LoginDTO loginDTO);
    List<FilmsResponse> retrieveAndSaveMovies();
    List<Films> retrieveAllMovies(Long id);
    SearchResponse searchMoviesByName(String keyword);
    RatingResponse rateMovie(Long movieId, Long userId, RateDTO rateDTO);
    Double calculateAverageRating(Long id);
    Double retrieveAverageRating(Long id);
    List<Films> getRecommendations(Long userId);
}
