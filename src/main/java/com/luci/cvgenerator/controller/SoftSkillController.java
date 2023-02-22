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

import com.luci.cvgenerator.entity.SoftSkill;
import com.luci.cvgenerator.entity.User;
import com.luci.cvgenerator.service.UserService;

@Controller
@RequestMapping("/softskill")
public class SoftSkillController {

	@Autowired
	UserService userService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {

		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@GetMapping("/displaySoftskill")
	public String displaySoftSkill(Principal principal, Model model) {
		User user = userService.findUserByUsername(principal.getName());

		if (user.getSoftSkillList() == null) {
			user.setSoftSkillList(new ArrayList<SoftSkill>());
		}

		model.addAttribute("softskills", user.getSoftSkillList());

		return "softskill/list-softskills";
	}

	@GetMapping("/addSoftskillForm")
	public String addSoftskillForm(Model model) {

		model.addAttribute("softskill", new SoftSkill());

		return "softskill/modify-softskills";

	}

	@PostMapping("/updateSoftskill")
	public String updateSoftSkill(Principal principal, @ModelAttribute("softskill") SoftSkill softSkill) {

		User user = userService.findUserByUsername(principal.getName());

		user.addSoftSkill(softSkill);

		userService.updateUser(user);

		return "redirect:/softskill/displaySoftskill";
	}

	@GetMapping("/deleteSoftskill")
	public String deleteSoftSkill(Principal principal, @RequestParam("softskillIndex") int softSkillIndex) {

		User user = userService.findUserByUsername(principal.getName());

		user.removeSoftSkill(softSkillIndex);

		userService.updateUser(user);

		return "redirect:/softskill/displaySoftskill";
	}

	@GetMapping("/updateSoftskillForm")
	public String updateSoftSkillForm(@RequestParam("softskillIndex") int softSkillIndex, Principal principal,
			Model model) {
		User user = userService.findUserByUsername(principal.getName());

		model.addAttribute("softskill", user.getSoftSkillList().get(softSkillIndex));

		return "softskill/modify-softskills";
	}

}
