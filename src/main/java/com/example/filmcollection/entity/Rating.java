package com.example.filmcollection.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "rating"
)
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int rate;
    private Double averageRate;
    private LocalDateTime ratedAt;
    private LocalDateTime updatedAt;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "film_id", referencedColumnName = "id")
    private Films films;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users users;

}
