package com.example.filmcollection.repository;

import com.example.filmcollection.entity.Films;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilmsRepository extends JpaRepository<Films, Long> {
    @Query(value = "select * from films where (upper(title) like UPPER(concat('%', :keyword, '%')))", nativeQuery = true)
    List<Films> findAllByTitle(String keyword);
    Optional<Films> findFilmsById(Long id);

}
