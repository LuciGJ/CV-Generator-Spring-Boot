package com.luci.cvgenerator.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.luci.cvgenerator.entity.User;
import com.luci.cvgenerator.service.UserService;
import com.luci.cvgenerator.utility.CV;
import com.luci.cvgenerator.utility.CVGenerator;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/cv")
public class CVController {

	@Autowired
	UserService userService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {

		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@GetMapping("/displayCVGenerator")
	public String displayCVGenerator(Model model) {
		model.addAttribute("cv", new CV());

		return "cv/generate-cv";
	}

	@PostMapping("/generateCV")
	public String generateCV(@ModelAttribute("cv") CV cv, Principal principal) {
		CVGenerator.generateCVPDF(userService, principal, cv);
		return "redirect:/cv/CVGenerated?file";
	}

	@GetMapping("/CVGenerated")
	public String CVGenerated() {
		return "cv/cv-generated-page";
	}

	@GetMapping("/displayCVBrowser")
	public void displayCVBrowser(Principal principal, HttpServletResponse response) {
		User user = userService.findUserByUsername(principal.getName());
		response.setContentType("application/pdf");
		InputStream inputStream;
		try {

			inputStream = new FileInputStream("user-cvs/" + user.getId() + ".pdf");
			int nRead;
			while ((nRead = inputStream.read()) != -1) {
				response.getWriter().write(nRead);
			}
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	@GetMapping("/downloadCV")
	public ResponseEntity<FileSystemResource> downloadCV(Principal principal) {
		User user = userService.findUserByUsername(principal.getName());
		File file = new File("user-cvs/" + user.getId() + ".pdf");
		long fileLength = file.length(); //

		HttpHeaders respHeaders = new HttpHeaders();
		respHeaders.setContentType(new MediaType("application", "pdf"));
		respHeaders.setContentLength(fileLength);
		respHeaders.setContentDispositionFormData("attachment", "CV.pdf");

		return new ResponseEntity<FileSystemResource>(new FileSystemResource(file), respHeaders, HttpStatus.OK);
	}

}
