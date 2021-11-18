package com.zensar.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zensar.model.Movie;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Integer> {

	public Movie findByMovieId(int movieId);

	public List<Movie> findAllByMovieNameAndDate(String movieName, LocalDate date);
}
