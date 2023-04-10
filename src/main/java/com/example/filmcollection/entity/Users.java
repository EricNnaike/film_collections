package com.example.filmcollection.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(
        name = "Users"
)
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    private String name;

    @Column(
            name = "email",
            unique = true
    )
    private String email;
    private String password;

    @JsonManagedReference
    @OneToMany(mappedBy = "users")
    List<Rating> ratingList = new ArrayList<>();

    public Users(Long id, String name, String email, String password) {
        Id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Users(Long id, String name, String email, String password, List<Rating> ratingList) {
        Id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.ratingList = ratingList;
    }

    public Users() {
    }
}
