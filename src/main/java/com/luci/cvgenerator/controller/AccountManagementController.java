package com.luci.cvgenerator.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luci.cvgenerator.account.Email;
import com.luci.cvgenerator.account.EmailDetails;
import com.luci.cvgenerator.account.ResetPassword;
import com.luci.cvgenerator.entity.User;
import com.luci.cvgenerator.service.EmailService;
import com.luci.cvgenerator.service.UserService;
import com.luci.cvgenerator.utility.LinkUtility;
import com.luci.cvgenerator.utility.RandomStringBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RequestMapping("/account")
@Controller
public class AccountManagementController {

	@Autowired
	UserService userService;

	@Autowired
	EmailService emailService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {

		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@GetMapping("/listOptions")
	public String listOptions() {
		return "account/account-options-page";
	}

	@GetMapping("/changeEmail")
	public String changeEmailPage(Model model) {

		model.addAttribute("email", new Email());
		return "account/change-email-page";
	}

	@PostMapping("/changeEmailProcess")
	public String changeEmailProcess(@Valid @ModelAttribute("email") Email email, BindingResult bindingResult,
			Model model, Principal principal, HttpServletRequest request) {

		if (bindingResult.hasErrors()) {
			return "account/change-email-page";
		}

		User user = userService.findUserByUsername(principal.getName());
		if (!email.getEmail().equals(user.getEmail())) {
			model.addAttribute("email", new Email());
			model.addAttribute("emailError", "The email address is wrong");
			return "account/change-email-page";
		}
		String token;
		do {
			token = RandomStringBuilder.buildRandomString(30);
		} while (userService.findByChangeEmailToken(token) != null);
		userService.updateChangeEmailToken(token, user.getEmail());
		String changeEmailLink = LinkUtility.getSiteURL(request) + "/account/changeEmailForm?token=" + token;
		EmailDetails details = new EmailDetails();

		details.setRecipient(user.getEmail());
		details.setMsgBody("Click the following link to change your email address: \n " + changeEmailLink);
		details.setSubject("CV Generator change email");

		emailService.sendEmail(details);
		model.addAttribute("message", "An email has been sent to confirm your identity.");
		return "account/confirm-management-page";

	}

	@GetMapping("/changeEmailForm")
	public String showNewEmail(@RequestParam("token") String token, HttpServletRequest request, Model model) {
		User user = userService.findByChangeEmailToken(token);
		Email email = new Email();
		email.setToken(token);
		model.addAttribute("email", email);

		if (user == null) {
			return "redirect:/";
		}

		return "account/new-email-page";
	}

	@PostMapping("/newEmailProcess")
	public String newEmailProcess(@Valid @ModelAttribute("email") Email email, BindingResult bindingResult, Model model,
			Principal principal) {

		if (bindingResult.hasErrors()) {
			return "account/new-email-page";
		}

		if (userService.findUserByEmail(email.getEmail()) != null) {
			model.addAttribute("emailError", "The email already belongs to an account");
			return "account/new-email-page";
		}

		User user = userService.findUserByUsername(principal.getName());

		if (user == null) {
			return "redirect:/login";
		}

		if (!user.getEmailToken().equals(email.getToken())) {
			model.addAttribute("message", "Invalid token");
			return "account/confirm-management-page";
		}

		user.setEmail(email.getEmail());
		user.setEmailToken(null);
		userService.updateUser(user);
		model.addAttribute("message", "Email changed successfully!");
		return "account/confirm-management-page";

	}

	@GetMapping("/changePassword")
	public String changePassword(Model model) {
		model.addAttribute("resetPassword", new ResetPassword());
		return "account/change-password-form";
	}

	@PostMapping("/changePasswordProcess")
	public String changePasswordProcess(@Valid @ModelAttribute("resetPassword") ResetPassword resetPassword,
			BindingResult bindingResult, Model model, Principal principal, BCryptPasswordEncoder passwordEncoder) {

		if (bindingResult.hasErrors()) {
			return "account/change-password-form";
		}

		User user = userService.findUserByUsername(principal.getName());

		if (user == null) {
			return "redirect:/login";
		}
		user.setPassword(passwordEncoder.encode(resetPassword.getPassword()));
		userService.updateUser(user);
		model.addAttribute("message", "Password changed successfully!");
		return "account/confirm-management-page";

	}

	@GetMapping("/deleteAccount")
	public String deleteAccount(Model model) {
		model.addAttribute("email", new Email());
		return "account/delete-account-page";
	}

	@PostMapping("/deleteAccountProcess")
	public String deleteAccountProcess(@Valid @ModelAttribute("email") Email email, BindingResult bindingResult,
			Model model, Principal principal, HttpServletRequest request) {

		if (bindingResult.hasErrors()) {
			return "account/delete-account-page";
		}

		User user = userService.findUserByUsername(principal.getName());

		if (user == null) {
			return "redirect:/login";
		}

		if (!user.getEmail().equals(email.getEmail())) {
			model.addAttribute("emailError", "The email address is wrong.");
			return "account/delete-account-page";
		}

		userService.remove(user);

		try {
			request.logout();
		} catch (ServletException e) {
			e.printStackTrace();
		}

		return "redirect:/deleteConfirm";

	}

}
