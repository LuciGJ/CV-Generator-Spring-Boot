package com.luci.cvgenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luci.cvgenerator.entity.User;
import com.luci.cvgenerator.service.UserService;

@Controller
public class MainController {

	@Autowired
	UserService userService;

	@GetMapping("/")
	public String getHome() {
		return "home";
	}

	@GetMapping("/menu")
	public String menu() {
		return "menu-page";
	}

	@GetMapping("/confirmAccount")
	public String confirmAccount(@RequestParam("token") String token, Model model) {
		User user = userService.findByConfirmationToken(token);
		if (user == null || user.getEnabled() == 1) {
			return "redirect:/login";
		}

		user.setEnabled(1);
		user.setConfirmationToken(null);
		userService.updateUser(user);

		model.addAttribute("message", "Account confirmed successfully!");
		return "email-confirm-page";
	}

	@GetMapping("/deleteConfirm")
	public String deleteConfirm() {
		return "account/delete-confirm-page";
	}

}
