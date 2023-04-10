package com.example.filmcollection.repository;

import com.example.filmcollection.entity.Films;
import com.example.filmcollection.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query(value = "SELECT * FROM rating WHERE user_id = ?1 AND film_id = ?2", nativeQuery = true)
    Rating findRatingByUserIdAndFilmId(Long userId, Long filmId);

    @Query(value = "SELECT * FROM rating WHERE film_id = ?1", nativeQuery = true)
    List<Rating> rateList(Long filmId);
    List<Rating> findRatingsByFilms(Films films);
    Optional<Rating> findRatingByFilms(Films films);
    Rating findRatingByAverageRate(Long id);
    List<Rating> findRatingByUsersId(Long userId);
}
