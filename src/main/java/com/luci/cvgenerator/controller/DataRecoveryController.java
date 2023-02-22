package com.luci.cvgenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
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
import com.luci.cvgenerator.account.Password;
import com.luci.cvgenerator.account.ResetPassword;
import com.luci.cvgenerator.entity.User;
import com.luci.cvgenerator.service.EmailService;
import com.luci.cvgenerator.service.UserService;
import com.luci.cvgenerator.utility.LinkUtility;
import com.luci.cvgenerator.utility.RandomStringBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RequestMapping("/recovery")
@Controller
public class DataRecoveryController {

	@Autowired
	UserService userService;

	@Autowired
	EmailService emailService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {

		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@GetMapping("/forgotConfirmation")
	public String forgotConfirmation() {
		return "recovery/forgot-confirmation";
	}

	@GetMapping("/forgotUsername")
	public String forgotUsername(Model model) {
		model.addAttribute("email", new Email());
		return "recovery/forgot-username";
	}

	@PostMapping("/forgotUsernameProcess")
	public String forgotUsernameProcess(@Valid @ModelAttribute("email") Email email, BindingResult bindingResult,
			Model model) {

		if (bindingResult.hasErrors()) {
			return "recovery/forgot-username";
		}

		User exists = userService.findUserByEmail(email.getEmail());
		if (exists == null) {
			model.addAttribute("email", new Email());
			model.addAttribute("emailError", "The email does not belong to any account");
			return "recovery/forgot-username";
		}

		EmailDetails details = new EmailDetails();

		details.setRecipient(email.getEmail());
		details.setMsgBody("Your username is: " + exists.getUsername());
		details.setSubject("CV Generator username");
		model.addAttribute("message", "An email containing your username has been sent to you.");
		emailService.sendEmail(details);
		return "recovery/confirm-page";

	}

	@GetMapping("/forgotPassword")
	public String showForgotPasswordForm(Model model) {
		model.addAttribute("password", new Password());
		return "recovery/forgot-password";
	}

	@PostMapping("/forgotPasswordProcess")
	public String forgotPasswordProcess(@Valid @ModelAttribute("password") Password password,
			BindingResult bindingResult, Model model, HttpServletRequest request) {

		if (bindingResult.hasErrors()) {
			return "recovery/forgot-password";
		}

		User exists = userService.findUserByUsername(password.getUsername());
		if (exists == null) {
			model.addAttribute("password", new Password());
			model.addAttribute("usernameError", "The username does not belong to any account");
			return "recovery/forgot-password";
		}

		String token;
		do {
			token = RandomStringBuilder.buildRandomString(30);
		} while (userService.findByResetPasswordToken(token) != null);
		userService.updateResetPasswordToken(token, exists.getEmail());
		String resetPasswordLink = LinkUtility.getSiteURL(request) + "/recovery/resetPassword?token=" + token;
		EmailDetails details = new EmailDetails();

		details.setRecipient(exists.getEmail());
		details.setMsgBody("Click the following link to reset your password: \n " + resetPasswordLink);
		details.setSubject("CV Generator password recovery");
		model.addAttribute("message", "An email containing a link to reset your password has been sent to you.");
		emailService.sendEmail(details);
		return "recovery/confirm-page";

	}

	@GetMapping("/resetPassword")
	public String showResetPasswordForm(@RequestParam("token") String token, HttpServletRequest request, Model model) {
		User user = userService.findByResetPasswordToken(token);
		ResetPassword resetPassword = new ResetPassword();
		resetPassword.setToken(token);
		model.addAttribute("resetPassword", resetPassword);

		if (user == null) {
			return "redirect:/";
		}

		return "recovery/reset-password-form";
	}

	@PostMapping("/resetPasswordProcess")
	public String resetPasswordProcess(@Valid @ModelAttribute("resetPassword") ResetPassword resetPassword,
			BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			return "recovery/reset-password-form";
		}

		String password = resetPassword.getPassword();
		User exists = userService.findByResetPasswordToken(resetPassword.getToken());
		if (exists == null) {
			return "redirect:/";
		}

		userService.updatePassword(exists.getUsername(), password);
		return "recovery/reset-confirmation";

	}

}
