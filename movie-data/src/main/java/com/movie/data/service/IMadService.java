package com.movie.data.service;

import java.util.List;

public interface IMadService {
    List<Long> mtoa(Long movieId);

    List<Long> mtod(Long movieId);

    List<Long> atom(Long actorId);

    List<Long> dtom(Long directorId);
}
