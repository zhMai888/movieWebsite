package com.movie.data.service;

import com.movie.data.domain.Movies;

import java.util.List;


public interface ICollectionsService {
    List<Movies> getCollectionsById(Long id);

    void addCollections(Long userId, Long movieId);

    void deleteCollections(Long userId, Long movieId);
}
