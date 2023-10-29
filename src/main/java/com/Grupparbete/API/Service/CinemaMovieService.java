package com.Grupparbete.API.Service;

import java.util.List;
import java.util.Optional;
import com.Grupparbete.API.Entities.Movie;

    public interface CinemaMovieService {

        Movie saveMovie(Movie movie);

        List<Movie> findAllMovies();

        Optional<Movie> findById(int id);
        void deleteMovieById(int customerId);
    }


