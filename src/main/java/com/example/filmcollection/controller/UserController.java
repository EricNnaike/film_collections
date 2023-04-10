package com.example.filmcollection.controller;

import com.example.filmcollection.dto.LoginDTO;
import com.example.filmcollection.dto.RateDTO;
import com.example.filmcollection.dto.UserDTO;
import com.example.filmcollection.entity.Films;
import com.example.filmcollection.entity.Users;
import com.example.filmcollection.response.FilmsResponse;
import com.example.filmcollection.response.LoginResponse;
import com.example.filmcollection.response.RatingResponse;
import com.example.filmcollection.response.SearchResponse;
import com.example.filmcollection.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Users> create(@RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.create(userDTO), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDTO loginDTO) {
        return new ResponseEntity<>(userService.login(loginDTO), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<List<FilmsResponse>> retrieveAndSaveMovies() {
        return new ResponseEntity<>(userService.retrieveAndSaveMovies(), HttpStatus.OK);
    }

    @GetMapping("/retrieve-all/{id}")
    public ResponseEntity<List<Films>> retrieveAllMovies(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.retrieveAllMovies(id), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<SearchResponse<?>> searchMovie(@RequestParam String keyword) {
        return new ResponseEntity<>(userService.searchMoviesByName(keyword), HttpStatus.OK);
    }

    @PostMapping("/rate/{movieId}")
    public ResponseEntity<RatingResponse> rateMovie(
            @PathVariable("movieId") Long movieId, @RequestParam Long userId, @RequestBody RateDTO rateDTO) {
        return new ResponseEntity<>(userService.rateMovie(movieId, userId, rateDTO), HttpStatus.OK);
    }

    @GetMapping("/average-rating/{id}")
    public ResponseEntity<Double> averageRating(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.calculateAverageRating(id), HttpStatus.OK);
    }

    @GetMapping("/average-rate/{id}")
    public ResponseEntity<Double> getAverageRate(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.calculateAverageRating(id), HttpStatus.OK);
    }

    @GetMapping("/get-recommendation/{userid}")
    public ResponseEntity<List<Films>> getRecommendation(@PathVariable("userid") Long id) {
        return new ResponseEntity<>(userService.getRecommendations(id), HttpStatus.OK);
    }

}
