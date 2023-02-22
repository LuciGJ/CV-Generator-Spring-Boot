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

import com.luci.cvgenerator.entity.HardSkill;
import com.luci.cvgenerator.entity.User;
import com.luci.cvgenerator.service.UserService;

@Controller
@RequestMapping("/hardskill")
public class HardSkillController {

	@Autowired
	UserService userService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {

		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@GetMapping("/displayHardskill")
	public String displayHardSkill(Principal principal, Model model) {
		User user = userService.findUserByUsername(principal.getName());

		if (user.getHardSkillList() == null) {
			user.setHardSkillList(new ArrayList<HardSkill>());
		}

		model.addAttribute("hardskills", user.getHardSkillList());

		return "hardskill/list-hardskills";
	}

	@GetMapping("/addHardskillForm")
	public String addHardskillForm(Model model) {

		model.addAttribute("hardskill", new HardSkill());

		return "hardskill/modify-hardskills";

	}

	@PostMapping("/updateHardskill")
	public String updateHardSkill(Principal principal, @ModelAttribute("hardskill") HardSkill hardSkill) {

		User user = userService.findUserByUsername(principal.getName());

		user.addHardSkill(hardSkill);

		userService.updateUser(user);

		return "redirect:/hardskill/displayHardskill";
	}

	@GetMapping("/deleteHardskill")
	public String deleteHardSkill(Principal principal, @RequestParam("hardskillIndex") int hardSkillIndex) {

		User user = userService.findUserByUsername(principal.getName());

		user.removeHardSkill(hardSkillIndex);

		userService.updateUser(user);

		return "redirect:/hardskill/displayHardskill";
	}

	@GetMapping("/updateHardskillForm")
	public String updateHardSkillForm(@RequestParam("hardskillIndex") int hardSkillIndex, Principal principal,
			Model model) {
		User user = userService.findUserByUsername(principal.getName());

		model.addAttribute("hardskill", user.getHardSkillList().get(hardSkillIndex));

		return "hardskill/modify-hardskills";
	}

}
