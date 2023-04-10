package com.example.filmcollection.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse<T> {
    private String message;
    private T DATA;
}
