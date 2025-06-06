package com.movie.data.domain;

import java.util.List;

public class MovieDTO {
    private Movies movies;
    private List<Long> actors;
    private List<Long> directors;

    public MovieDTO() {
    }

    public MovieDTO(Movies movies, List<Long> actors, List<Long> directors) {
        this.movies = movies;
        this.actors = actors;
        this.directors = directors;
    }

    public Movies getMovies() {
        return movies;
    }

    public void setMovies(Movies movies) {
        this.movies = movies;
    }

    public List<Long> getActors() {
        return actors;
    }

    public void setActors(List<Long> actors) {
        this.actors = actors;
    }

    public List<Long> getDirectors() {
        return directors;
    }

    public void setDirectors(List<Long> directors) {
        this.directors = directors;
    }

    @Override
    public String toString() {
        return "MovieDTO{" +
                "movies=" + movies +
                ", actors=" + actors +
                ", directors=" + directors +
                '}';
    }
}
