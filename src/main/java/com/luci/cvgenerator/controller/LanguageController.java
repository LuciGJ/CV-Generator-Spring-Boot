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

import com.luci.cvgenerator.entity.Language;
import com.luci.cvgenerator.entity.User;
import com.luci.cvgenerator.service.UserService;

@Controller
@RequestMapping("/language")
public class LanguageController {

	@Autowired
	UserService userService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {

		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@GetMapping("/displayLanguages")
	public String displayLanguages(Principal principal, Model model) {
		User user = userService.findUserByUsername(principal.getName());

		if (user.getLanguageList() == null) {
			user.setLanguageList(new ArrayList<Language>());
		}

		model.addAttribute("languages", user.getLanguageList());

		return "languages/list-languages";
	}

	@GetMapping("/addLanguageForm")
	public String addLanguageForm(Model model) {

		model.addAttribute("language", new Language());

		return "languages/modify-language";

	}

	@PostMapping("/updateLanguage")
	public String updateLanguage(Principal principal, @ModelAttribute("language") Language language) {

		User user = userService.findUserByUsername(principal.getName());

		user.addLanguage(language);

		userService.updateUser(user);

		return "redirect:/language/displayLanguages";
	}

	@GetMapping("/deleteLanguage")
	public String deleteLanguage(Principal principal, @RequestParam("languageIndex") int languageIndex) {

		User user = userService.findUserByUsername(principal.getName());

		user.removeLanguage(languageIndex);

		userService.updateUser(user);

		return "redirect:/language/displayLanguages";
	}

	@GetMapping("/updateLanguageForm")
	public String updateLanguageForm(@RequestParam("languageIndex") int languageIndex, Principal principal,
			Model model) {
		User user = userService.findUserByUsername(principal.getName());

		model.addAttribute("language", user.getLanguageList().get(languageIndex));

		return "languages/modify-language";
	}

}
