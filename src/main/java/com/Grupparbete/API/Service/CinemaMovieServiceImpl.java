package com.Grupparbete.API.Service;

import com.Grupparbete.API.DAO.MovieRepository;
import com.Grupparbete.API.Entities.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CinemaMovieServiceImpl implements CinemaMovieService {

 @Autowired
 MovieRepository movieRepository;


    @Override
    public Movie saveMovie(Movie movie) {
        System.out.println("Saved movie");
        return movieRepository.save(movie);
    }

    @Override
    public List<Movie> findAllMovies() {
        System.out.println("Found all Movies");
        return movieRepository.findAll();
    }

    @Override
    public Optional<Movie> findById(int id) {
        return movieRepository.findById(id);
    }

    @Override
    public void deleteMovieById(int customerId) {
        System.out.println("Removed movie from database");
        movieRepository.deleteById(customerId);
    }
}

