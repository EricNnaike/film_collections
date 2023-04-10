package com.example.filmcollection.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Blob;
import java.sql.Clob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "films"
)
public class Films {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String releaseDate;
    @Column(name = "overview", columnDefinition = "Clob")
    private String overview;
    private String posterPath;
    private Long popularity;
    private Double averageRating;

    @JsonManagedReference
    @OneToMany(mappedBy = "films")
    List<Rating> ratingList = new ArrayList<>();

}
