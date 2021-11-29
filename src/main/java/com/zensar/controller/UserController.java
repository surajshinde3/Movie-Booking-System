package com.zensar.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zensar.model.User;
import com.zensar.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/getregister")
	public String getRegisterPage() {

		return "register";
	}

	@GetMapping("/login")
	public String getLoginPage() {
		return "login";
	}

	@RequestMapping("/default")
	public String defaultAfterLogin(HttpServletRequest request) {
		if (request.isUserInRole("ROLE_ADMIN")) {
			return "redirect:/viewMovies?login";
		}
		return "redirect:/?login";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerUserAccount(@Valid @ModelAttribute User user, Model model) {

		if (userService.checkUserExists(user.getUsername(), user.getEmail())) {

			if (userService.checkEmailExists(user.getEmail())) {
				model.addAttribute("existingemail", user.getEmail());
			}

			if (userService.checkUsernameExists(user.getUsername())) {
				model.addAttribute("usernameExists", user.getUsername());
			}
			return "register";
		} else {
			userService.saveUser(user);
			return "redirect:/login";
		}
	}

	@RequestMapping("/test")
	public String test() {

		return "indexpage";
	}

}
