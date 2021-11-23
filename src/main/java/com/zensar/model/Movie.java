package com.zensar.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "movies")
public class Movie {

	@Id
	@GeneratedValue
	private int movieId;
	private String movieName;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate releaseDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;
	private String time;
	private int duration;
	private String quality;
	private String language;
	private String type;
	private String screen;
	private int seats;
	private int price;

	@Column(columnDefinition = "boolean default false")
	private boolean isDeleted;

	@OneToMany(mappedBy = "movie")
	List<Customer> customers = new ArrayList<>();

	public Movie() {
		super();
	}

	public Movie(int movieId, String movieName, LocalDate releaseDate, LocalDate date, String time, int duration,
			String quality, String language, String type, String screen, int seats, int price, boolean isDeleted) {
		super();
		this.movieId = movieId;
		this.movieName = movieName;
		this.releaseDate = releaseDate;
		this.date = date;
		this.time = time;
		this.duration = duration;
		this.quality = quality;
		this.language = language;
		this.type = type;
		this.screen = screen;
		this.seats = seats;
		this.price = price;
		this.isDeleted = isDeleted;
	}

	public Movie(int movieId, String movieName, LocalDate releaseDate, LocalDate date, String time, int duration,
			String quality, String language, String type, String screen, int seats, int price, boolean isDeleted,
			List<Customer> customers) {
		super();
		this.movieId = movieId;
		this.movieName = movieName;
		this.releaseDate = releaseDate;
		this.date = date;
		this.time = time;
		this.duration = duration;
		this.quality = quality;
		this.language = language;
		this.type = type;
		this.screen = screen;
		this.seats = seats;
		this.price = price;
		this.isDeleted = isDeleted;
		this.customers = customers;
	}

	public int getMovieId() {
		return movieId;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public List<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}

	public String getScreen() {
		return screen;
	}

	public void setScreen(String screen) {
		this.screen = screen;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}
