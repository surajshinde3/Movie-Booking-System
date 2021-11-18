package com.zensar.controller;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zensar.model.Customer;
import com.zensar.model.Movie;
import com.zensar.model.User;
import com.zensar.service.CustomerService;
import com.zensar.service.MovieService;
import com.zensar.service.UserService;

@Controller
public class MovieController {

	@Autowired
	private MovieService service;

	@Autowired
	private CustomerService cservice;

	@Autowired
	private UserService uservice;

	@GetMapping("/movie/delete")
	public String deleteMovie(@PathParam("movieId") int movieId) {

		Movie m = service.findMovieById(movieId);
		m.setDeleted(true);
		service.insertMovie(m);
		return "redirect:/viewMovies";
	}

	@GetMapping("/movie/edit")
	public String editMovie(@PathParam("movieId") int movieId, Model model) {
		Movie movie = service.findMovieById(movieId);
		model.addAttribute("movie", movie);
		return "editMovie";
	}

	@PostMapping("/movie/edit")
	public String editMovie(@PathParam(value = "movieId") int movieId, @ModelAttribute("movie") Movie movie,
			BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("errors", bindingResult.getAllErrors());
			model.addAttribute("movie", new Movie());
			return "newMovie";
		}
		service.editMovie(movie);
		model.addAttribute("movie", movie);
		return "redirect:/viewMovies";
	}

	@GetMapping("/editMovie")
	public String getEditMovie(Model model) {
		return "editMovie";
	}

	@GetMapping("/newMovie")
	public String newMovie(Model model) {
		model.addAttribute("movie", new Movie());
		return "newMovie";
	}

	public List<Movie> timeConvert(Iterable<Movie> list) {
		List<Movie> movies = new ArrayList<Movie>();
		for (Movie m : service.getAllMovies()) {
			String time = m.getTime();

			try {
				final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
				final Date dateObj = sdf.parse(time);
				String newTime = new SimpleDateFormat("hh:mm a").format(dateObj);
				m.setTime(newTime);
				movies.add(m);
			} catch (final ParseException e) {
				e.printStackTrace();
			}
		}
		return movies;
	}

	public String singleTimeConvert(String time) {
		String newTime = null;
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
			final Date dateObj = sdf.parse(time);
			newTime = new SimpleDateFormat("hh:mm a").format(dateObj);
		} catch (final ParseException e) {
			e.printStackTrace();
		}
		return newTime;
	}

	@GetMapping("/viewMovies")
	public String viewMovie(Model model) {

		List<Movie> movies = timeConvert(service.getAllMovies());

		List<Movie> moviesDeleted = new ArrayList<Movie>();

		Collections.sort(movies, new Comparator<Movie>() {
			public int compare(Movie o1, Movie o2) {
				return o2.getDate().compareTo(o1.getDate());
			}
		});

		for (Movie m : movies) {
			if (!m.isDeleted()) {
				moviesDeleted.add(m);
			}
		}

		List<Movie> moviess = getSeatCount(moviesDeleted);

		model.addAttribute("moviess", moviess);
		return "movies";
	}

	public List<Movie> getSeatCount(List<Movie> movies) {

		List<Movie> moviess = new ArrayList<Movie>();

		for (Movie m : movies) {
			List<Customer> customers = m.getCustomers();
			List<Customer> customersActive = new ArrayList<Customer>();
			for (Customer c : customers) {
				if (!c.isCancelled()) {
					customersActive.add(c);

				}
			}
			m.setCustomers(customersActive);
			moviess.add(m);
		}
		return movies;

	}

	@GetMapping("/bookTicket")
	public String bookTicket(Model model) {
		List<Movie> movies = timeConvert(service.getAllMovies());

		List<Movie> moviesDeleted = new ArrayList<Movie>();

		Collections.sort(moviesDeleted, new Comparator<Movie>() {
			public int compare(Movie o1, Movie o2) {
				return o2.getDate().compareTo(o1.getDate());
			}
		});

		for (Movie m : movies) {
			if (!m.isDeleted()) {
				moviesDeleted.add(m);
			}
		}

		List<Movie> moviess = getSeatCount(moviesDeleted);

		model.addAttribute("movies", moviess);
		return "bookTicket";
	}

	@PostMapping("/movie/new")
	public String saveMovie(@Valid @ModelAttribute("movie") Movie movie, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("errors", bindingResult.getAllErrors());
			model.addAttribute("movie", new Movie());
			return "newMovie";
		}
		service.insertMovie(movie);
		model.addAttribute("movie", movie);
		return "redirect:/viewMovies";
	}

	@GetMapping("/movie/book/new")
	public String showCustomerInfoPage(@RequestParam int movieId, Model model) {
		model.addAttribute("movieId", movieId);
		model.addAttribute("customer", new Customer());
		return "newCustomer";
	}

	@PostMapping("/movie/book/new")
	public String bookTicket(@Valid @ModelAttribute("customer") Customer customer, BindingResult bindingResult,
			@RequestParam("movieId") int movieId, Model model, Principal principal) {
		Movie movie = service.findMovieById(movieId);

		String t = singleTimeConvert(movie.getTime());
		movie.setTime(t);

		User user = uservice.findByUsername(principal.getName());

		Customer customer1 = customer;

		customer1.setUser(user);
		customer1.setMovie(movie);
		cservice.saveCustomer(customer1);
		model.addAttribute("customer", customer1);
		return "confirmationPage";
	}

	@GetMapping("/customers")
	public String showCustomerList(@RequestParam int movieId, Model model) {
		List<Customer> customers = service.findMovieById(movieId).getCustomers();

		List<Customer> customerBooked = new ArrayList<Customer>();

		for (Customer c : customers) {

			if (!c.isCancelled()) {
				customerBooked.add(c);
			}

		}
		model.addAttribute("customers", customerBooked);
		Movie movie = service.findMovieById(movieId);

		String t = singleTimeConvert(movie.getTime());
		movie.setTime(t);
		model.addAttribute("movie", movie);

		return "customers";
	}

	@GetMapping("/add")
	public String getAddPage(Model model) {
		return "add";
	}

	@GetMapping("/viewMovie")
	public String viewMovie(@RequestParam int movieId, Model model) {
		Movie movie = service.findMovieById(movieId);
		Movie mov = getSeatCount(movie);

		String t = singleTimeConvert(mov.getTime());
		mov.setTime(t);
		model.addAttribute("movie", mov);
		return "viewMovie";
	}

	@GetMapping("/home")
	public String homePage(Model model) {
		Iterable<Movie> movies = service.getAllMovies();
		List<Movie> moviesDeleted = new ArrayList<Movie>();

		for (Movie m : movies) {
			if (!m.isDeleted()) {
				moviesDeleted.add(m);
			}
		}

		model.addAttribute("movies", moviesDeleted);
		return "home";
	}

	@PostMapping("/movie/search")
	public String searchMovieToBook(@RequestParam(value = "movieId") int movieId, @RequestParam("date") String date,
			Model model) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate deptTime = LocalDate.parse(date, dtf);

		model.addAttribute("movies", service.getAllMovies());

		Movie movie = service.findMovieById(movieId);
		List<Movie> list = service.findAllByMovieNameAndDate(movie.getMovieName(), deptTime);

		List<Movie> moviesDeleted = new ArrayList<Movie>();

		for (Movie m : list) {
			if (!m.isDeleted()) {
				moviesDeleted.add(m);
			}
		}

		List<Movie> moviess = getSeatCount(moviesDeleted);

		if (list.isEmpty()) {
			model.addAttribute("notFound", "No Record Found!");
		} else {
			model.addAttribute("movieslist", moviess);
		}

		return "home";
	}

	public Movie getSeatCount(Movie movie) {

		List<Customer> customers = movie.getCustomers();
		List<Customer> customersActive = new ArrayList<Customer>();
		for (Customer c : customers) {
			if (!c.isCancelled()) {
				customersActive.add(c);

			}
		}
		movie.setCustomers(customersActive);
		return movie;
	}

	@GetMapping("/adminViewMovie")
	public String adminViewMovie(@RequestParam int movieId, Model model) {
		Movie movie = service.findMovieById(movieId);

		Movie seatCount = getSeatCount(movie);

		String t = singleTimeConvert(seatCount.getTime());
		movie.setTime(t);
		model.addAttribute("movie", seatCount);
		return "adminViewMovie";
	}

	@GetMapping("/adminViewDeletedMovie")
	public String adminViewDeletedMovie(@RequestParam int movieId, Model model) {
		Movie movie = service.findMovieById(movieId);

		Movie seatCount = getSeatCount(movie);

		String t = singleTimeConvert(seatCount.getTime());
		movie.setTime(t);
		model.addAttribute("movie", seatCount);
		return "viewDeletedMovie";
	}

	@GetMapping("/bookedTickets")
	public String bookedTickets(Model model, Principal principal) {

		User user = uservice.findByUsername(principal.getName());
		List<Customer> customers = user.getCustomers();
		List<Customer> customerBooked = new ArrayList<Customer>();

		for (Customer c : customers) {

			if (!c.isCancelled()) {
				customerBooked.add(c);
			}

		}
		model.addAttribute("customers", customerBooked);

		List<Movie> movies = timeConvert(service.getAllMovies());
		Collections.sort(movies, new Comparator<Movie>() {
			public int compare(Movie o1, Movie o2) {
				return o2.getDate().compareTo(o1.getDate());
			}
		});
		model.addAttribute("movies", movies);

		return "bookedTicket";
	}

	@GetMapping("/viewBookedTicket")
	public String viewBookedTicket(@RequestParam Long customerId, Model model, Principal principal) {

		User user = uservice.findByUsername(principal.getName());
		List<Customer> customers = user.getCustomers();
		model.addAttribute("customers", customers);

		List<Movie> movies = timeConvert(service.getAllMovies());
		Collections.sort(movies, new Comparator<Movie>() {
			public int compare(Movie o1, Movie o2) {
				return o2.getDate().compareTo(o1.getDate());
			}
		});
		model.addAttribute("movies", movies);

		Customer customer = cservice.getCustomerById(customerId);

		model.addAttribute("customer", customer);
		return "viewBookedTicket";
	}

	@GetMapping("/cancelTicket")
	public String cancelTicket(@RequestParam Long customerId, Model model, Principal principal) {

		Customer customer = cservice.getCustomerById(customerId);
		customer.setCancelled(true);
		cservice.saveCustomer(customer);
		model.addAttribute("customer", customer);
		return "redirect:/bookedTickets";
	}

	@GetMapping("/cancelledTickets")
	public String getCalcelledTickets(Model model, Principal principal) {

		User user = uservice.findByUsername(principal.getName());
		List<Customer> customers = user.getCustomers();
		List<Customer> customerCancelled = new ArrayList<Customer>();

		for (Customer c : customers) {

			if (c.isCancelled()) {
				customerCancelled.add(c);
			}

		}
		model.addAttribute("customers", customerCancelled);

		List<Movie> movies = timeConvert(service.getAllMovies());
		Collections.sort(movies, new Comparator<Movie>() {
			public int compare(Movie o1, Movie o2) {
				return o2.getDate().compareTo(o1.getDate());
			}
		});
		model.addAttribute("movies", movies);

		return "cancelledTickets";
	}

	@GetMapping("/deletedMovies")
	public String getDeletedMovies(Model model) {

		List<Movie> movies = timeConvert(service.getAllMovies());

		List<Movie> moviesDeleted = new ArrayList<Movie>();

		Collections.sort(movies, new Comparator<Movie>() {
			public int compare(Movie o1, Movie o2) {
				return o2.getDate().compareTo(o1.getDate());
			}
		});

		for (Movie m : movies) {
			if (m.isDeleted()) {
				moviesDeleted.add(m);
			}
		}

		List<Movie> moviess = getSeatCount(moviesDeleted);

		model.addAttribute("moviess", moviess);
		return "deletedMovies";
	}

}