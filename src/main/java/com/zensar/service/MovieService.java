package com.zensar.service;

import java.time.LocalDate;
import java.util.List;

import com.zensar.model.Movie;

public interface MovieService {

	public Movie findMovieById(int movieId);

	public void deleteMovie(int movieId);

	public Movie insertMovie(Movie movie);

	public Iterable<Movie> getAllMovies();
	
	public Movie editMovie(Movie movie);
	
	public List<Movie> findAllByMovieNameAndDate(String movieName, LocalDate date);

}
