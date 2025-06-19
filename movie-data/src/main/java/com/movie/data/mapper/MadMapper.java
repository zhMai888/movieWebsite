package com.movie.data.mapper;

import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

public interface MadMapper {
    List<Long> mtoa(@Param("movieId") Long movieId);

    List<Long> mtod(@Param("movieId") Long movieId);

    List<Long> atom(@Param("actorId") Long actorId);

    List<Long> dtom(@Param("directorId") Long directorId);
}
