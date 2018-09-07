package com.guide.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.guide.model.User;
import com.guide.service.UserService;

@Controller
public class HomeController {

	@Autowired
	private UserService userService;

	@GetMapping({ "/", "/index" })
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("index");
		modelAndView.addObject("name", "Gil");
		return modelAndView;
	}

	@GetMapping("/login")
	public ModelAndView login() {

		ModelAndView modelAndView = new ModelAndView();			
		modelAndView.setViewName("user/login");
		return modelAndView;
	}

	@GetMapping("/signup")
	public ModelAndView signUp() {

		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		modelAndView.addObject("user", user);
		modelAndView.setViewName("user/signup");
		return modelAndView;
	}

	@PostMapping("/user/signup")
	public ModelAndView createUser(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userService.findUserByEmail(user.getEmail());

		if (userExists != null) {
			bindingResult.rejectValue("email", "error.user", "This email already exists!");
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("/index");
		} else {
			userService.saveUser(user);
			modelAndView.addObject("msg", "User has been registered successfully!");
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("home/home");
		}

		return modelAndView;
	}

	@GetMapping("/home/home")
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());

		modelAndView.addObject("userName", user.getFirstname() + " " + user.getLastname());
		modelAndView.setViewName("home/home");
		return modelAndView;
	}

	@GetMapping("/access_denied")
	public ModelAndView accessDenied() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("errors/access_denied");
		return modelAndView;
	}
}
