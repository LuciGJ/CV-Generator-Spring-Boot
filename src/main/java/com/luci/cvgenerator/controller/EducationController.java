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

import com.luci.cvgenerator.entity.Education;
import com.luci.cvgenerator.entity.User;
import com.luci.cvgenerator.service.UserService;

@Controller
@RequestMapping("/education")
public class EducationController {

	@Autowired
	UserService userService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {

		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@GetMapping("/displayEducation")
	public String displayEducation(Principal principal, Model model) {
		User user = userService.findUserByUsername(principal.getName());

		if (user.getEducationList() == null) {
			user.setEducationList(new ArrayList<Education>());
		}

		model.addAttribute("educations", user.getEducationList());

		return "education/list-education";
	}

	@GetMapping("/addEducationForm")
	public String addEducationForm(Model model) {

		model.addAttribute("education", new Education());

		return "education/modify-education";

	}

	@PostMapping("/updateEducation")
	public String updateEducation(Principal principal, @ModelAttribute("education") Education education) {

		User user = userService.findUserByUsername(principal.getName());

		user.addEducation(education);

		userService.updateUser(user);

		return "redirect:/education/displayEducation";
	}

	@GetMapping("/deleteEducation")
	public String deleteEducation(Principal principal, @RequestParam("educationIndex") int educationIndex) {

		User user = userService.findUserByUsername(principal.getName());

		user.removeEducation(educationIndex);

		userService.updateUser(user);

		return "redirect:/education/displayEducation";
	}

	@GetMapping("/updateEducationForm")
	public String updateEducationForm(@RequestParam("educationIndex") int educationIndex, Principal principal,
			Model model) {
		User user = userService.findUserByUsername(principal.getName());

		model.addAttribute("education", user.getEducationList().get(educationIndex));

		return "education/modify-education";
	}

}
