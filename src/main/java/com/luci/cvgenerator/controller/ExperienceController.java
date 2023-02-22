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

import com.luci.cvgenerator.entity.Experience;
import com.luci.cvgenerator.entity.User;
import com.luci.cvgenerator.service.UserService;

@Controller
@RequestMapping("/experience")
public class ExperienceController {

	@Autowired
	UserService userService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {

		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@GetMapping("/displayExperience")
	public String displayExperience(Principal principal, Model model) {
		User user = userService.findUserByUsername(principal.getName());

		if (user.getExperienceList() == null) {
			user.setExperienceList(new ArrayList<Experience>());
		}

		model.addAttribute("experiences", user.getExperienceList());

		return "experience/list-experience";
	}

	@GetMapping("/addExperienceForm")
	public String addExperienceForm(Model model) {

		model.addAttribute("experience", new Experience());

		return "experience/modify-experience";

	}

	@PostMapping("/updateExperience")
	public String updateExperience(Principal principal, @ModelAttribute("experience") Experience experience) {

		User user = userService.findUserByUsername(principal.getName());

		user.addExperience(experience);

		userService.updateUser(user);

		return "redirect:/experience/displayExperience";
	}

	@GetMapping("/deleteExperience")
	public String deleteExperience(Principal principal, @RequestParam("experienceIndex") int experienceIndex) {

		User user = userService.findUserByUsername(principal.getName());

		user.removeExperience(experienceIndex);

		userService.updateUser(user);

		return "redirect:/experience/displayExperience";
	}

	@GetMapping("/updateExperienceForm")
	public String updateExperienceForm(@RequestParam("experienceIndex") int experienceIndex, Principal principal,
			Model model) {
		User user = userService.findUserByUsername(principal.getName());

		model.addAttribute("experience", user.getExperienceList().get(experienceIndex));

		return "experience/modify-experience";
	}

}
