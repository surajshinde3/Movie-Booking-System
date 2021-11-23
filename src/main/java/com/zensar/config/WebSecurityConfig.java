package com.zensar.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Qualifier("userServiceImpl")
	@Autowired
	private UserDetailsService userService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(NoOpPasswordEncoder.getInstance());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().sameOrigin().and().authorizeRequests()
				.antMatchers("/", "/getregister", "/img/**", "/register", "/error","/default","/upcoming").permitAll()
				.antMatchers("/movie/book**", "/bookTicket", "/movie/book/new", "/viewMovie", "/search", "/movie/search",
						"/bookedTickets", "/viewBookedTicket", "/cancelTicket", "/cancelledTickets", "/editProfile",
						"/update","/home")
				.hasRole("USER")
				.antMatchers("/**", "/viewMovies", "/newMovie", "/movie/new", "/editMovie", "/movie/edit",
						"/adminViewMovie", "/deletedMovies", "/adminViewDeletedMovie","/viewCompletedMovies","/adminViewCompletedMovie")
				.hasRole("ADMIN")

				.anyRequest().authenticated().and().formLogin().loginPage("/login").defaultSuccessUrl("/default")
				.failureUrl("/login?error").permitAll().and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/?logout")
				.permitAll().and().csrf().disable().exceptionHandling();
	}
}