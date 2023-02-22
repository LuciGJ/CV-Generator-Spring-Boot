package com.luci.cvgenerator.controller;

import java.security.Principal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luci.cvgenerator.entity.Interest;
import com.luci.cvgenerator.entity.User;
import com.luci.cvgenerator.service.UserService;

@Controller
@RequestMapping("/interest")
public class InterestController {

	@Autowired
	UserService userService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {

		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@GetMapping("/displayInterest")
	public String displayInterest(Principal principal, Model model) {
		User user = userService.findUserByUsername(principal.getName());

		if (user.getInterestList() == null) {
			user.setInterestList(new ArrayList<Interest>());
		}

		model.addAttribute("interests", user.getInterestList());

		return "interest/list-interests";
	}

	@GetMapping("/addInterestForm")
	public String addInterestForm(Model model) {

		model.addAttribute("interest", new Interest());

		return "interest/modify-interests";

	}

	@PostMapping("/updateInterest")
	public String updateInterest(Principal principal, @ModelAttribute("interest") Interest interest) {

		User user = userService.findUserByUsername(principal.getName());

		user.addInterest(interest);

		userService.updateUser(user);

		return "redirect:/interest/displayInterest";
	}

	@GetMapping("/deleteInterest")
	public String deleteInterest(Principal principal, @RequestParam("interestIndex") int interestIndex) {

		User user = userService.findUserByUsername(principal.getName());

		user.removeInterest(interestIndex);

		userService.updateUser(user);

		return "redirect:/interest/displayInterest";
	}

	@GetMapping("/updateInterestForm")
	public String updateInterestForm(@RequestParam("interestIndex") int interestIndex, Principal principal,
			Model model) {
		User user = userService.findUserByUsername(principal.getName());

		model.addAttribute("interest", user.getInterestList().get(interestIndex));

		return "interest/modify-interests";
	}

}
