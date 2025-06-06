package com.movie.data.mapper;

import com.movie.data.domain.Movies;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CollectionsMapper {
    public List<Movies> selectById(@Param("userId") Long id);

    void addById(@Param("userId") Long userId, @Param("movieId") Long movieId);

    void deleteById(@Param("userId") Long userId, @Param("movieId") Long movieId);
}
