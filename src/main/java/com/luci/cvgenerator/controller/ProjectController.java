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

import com.luci.cvgenerator.entity.Project;
import com.luci.cvgenerator.entity.User;
import com.luci.cvgenerator.service.UserService;

@Controller
@RequestMapping("/project")
public class ProjectController {

	@Autowired
	UserService userService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {

		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@GetMapping("/displayProject")
	public String displayProject(Principal principal, Model model) {
		User user = userService.findUserByUsername(principal.getName());

		if (user.getProjectList() == null) {
			user.setProjectList(new ArrayList<Project>());
		}

		model.addAttribute("projects", user.getProjectList());

		return "project/list-projects";
	}

	@GetMapping("/addProjectForm")
	public String addProjectForm(Model model) {

		model.addAttribute("project", new Project());

		return "project/modify-projects";

	}

	@PostMapping("/updateProject")
	public String updateProject(Principal principal, @ModelAttribute("project") Project project) {

		User user = userService.findUserByUsername(principal.getName());

		user.addProject(project);

		userService.updateUser(user);

		return "redirect:/project/displayProject";
	}

	@GetMapping("/deleteProject")
	public String deleteProject(Principal principal, @RequestParam("projectIndex") int projectIndex) {

		User user = userService.findUserByUsername(principal.getName());

		user.removeProject(projectIndex);

		userService.updateUser(user);

		return "redirect:/project/displayProject";
	}

	@GetMapping("/updateProjectForm")
	public String updateProjectForm(@RequestParam("projectIndex") int projectIndex, Principal principal, Model model) {
		User user = userService.findUserByUsername(principal.getName());

		model.addAttribute("project", user.getProjectList().get(projectIndex));

		return "project/modify-projects";
	}

}
