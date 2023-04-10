package com.example.filmcollection.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilmsResponse {
    private String title;
    private String releaseDate;
    private String overview;
    private String posterPath;
}
