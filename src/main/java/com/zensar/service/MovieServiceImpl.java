package com.zensar.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.zensar.model.Movie;
import com.zensar.repository.MovieRepository;

@Service
public class MovieServiceImpl implements MovieService {

	@Autowired
	private MovieRepository repository;

	public Iterable<Movie> getAllMovies() {
		return repository.findAll();
	}

	public void deleteMovie(int movieId) {
		repository.deleteById(movieId);
	}

	public Movie insertMovie(Movie movie) {
		return repository.save(movie);
	}

	@Override
	public Movie findMovieById(int movieId) {
		return repository.findByMovieId(movieId);
	}

	@Override
	public Movie editMovie(Movie movie) {
		return repository.save(movie);
	}

	@Override
	public List<Movie> findAllByMovieNameAndDate(String movieName, LocalDate date) {
		return repository.findAllByMovieNameAndDate(movieName, date);
	}

}
