package com.luci.cvgenerator.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luci.cvgenerator.entity.User;
import com.luci.cvgenerator.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired 
	UserService userService;
	
	
	@GetMapping("/administrationPage")
	public String administrationPage(Model model, Principal principal) {
		model.addAttribute("users", userService.findAllUsers(principal.getName()));
		
		return "admin/administration-page";
	}

	@GetMapping("/suspendUser")
	public String suspendUser(@RequestParam("userId") int userId) {
		
		User user = userService.findUserById(userId);
		
		
		if(user.getSuspended() == 1) {
			user.setSuspended(0);
		} else {
			user.setSuspended(1);
		}
		
		userService.updateUser(user);
		
		return "redirect:/admin/administrationPage";
	}
	
	@GetMapping("/deleteUser")
	public String deleteUser(@RequestParam("userId") int userId) {
		
		User user = userService.findUserById(userId);
		
		
		userService.remove(user);
		
		return "redirect:/admin/administrationPage";
	}
	
	@GetMapping("/search")
	public String delete(@RequestParam("username") String username,
						 Model model, Principal principal) {
		
		List<User> users = userService.searchUser(principal.getName(), username);
		

		model.addAttribute("users", users);
		
		
		return "admin/administration-page";
		
	}
}
