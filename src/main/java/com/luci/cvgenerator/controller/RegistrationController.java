package com.luci.cvgenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.luci.cvgenerator.entity.User;
import com.luci.cvgenerator.service.EmailService;
import com.luci.cvgenerator.service.UserService;
import com.luci.cvgenerator.user.CVUser;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegistrationController {

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {

		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@GetMapping("/registrationForm")
	public String registrationForm(Model theModel) {
		theModel.addAttribute("user", new CVUser());
		return "registration-page";
	}

	@PostMapping("/registrationProcess")
	public String registrationProcess(@Valid @ModelAttribute("user") CVUser user, BindingResult bindingResult,
			Model model, HttpServletRequest request) {

		String username = user.getUsername();

		if (bindingResult.hasErrors()) {
			return "registration-page";
		}

		User exists = userService.findUserByUsername(username);
		if (exists != null) {
			model.addAttribute("user", new CVUser());
			model.addAttribute("registrationError", "Username already taken");
			return "registration-page";
		}

		exists = userService.findUserByEmail(user.getEmail());

		if (exists != null) {
			model.addAttribute("user", new CVUser());
			model.addAttribute("registrationError", "Email already used");
			return "registration-page";
		}

		userService.saveUser(user, emailService, request);

		return "registration-confirmation";

	}

}
