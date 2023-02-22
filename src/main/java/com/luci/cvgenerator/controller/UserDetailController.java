package com.luci.cvgenerator.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.luci.cvgenerator.entity.User;
import com.luci.cvgenerator.entity.UserDetail;
import com.luci.cvgenerator.service.UserService;
import com.luci.cvgenerator.utility.FileUploadUtil;

@Controller
@RequestMapping("/userdata")
public class UserDetailController {

	@Autowired
	UserService userService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {

		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@GetMapping("/listDetail")
	public String showHomePage(Principal principal, Model model) {

		User user = userService.findUserByUsername(principal.getName());

		model.addAttribute("userDetail", user.getUserDetail());

		return "userdetails/list-user-details";
	}

	@GetMapping("/updateDetailForm")
	public String updateDetailForm(Principal principal, Model model) {

		User user = userService.findUserByUsername(principal.getName());

		model.addAttribute("userDetail", user.getUserDetail());
		return "userdetails/modify-details";
	}

	@PostMapping("/updateDetail")
	public String updateDetail(Principal principal, @ModelAttribute("userDetail") UserDetail userDetail) {

		User user = userService.findUserByUsername(principal.getName());

		user.setUserDetail(userDetail);

		userService.updateUser(user);

		return "redirect:/userdata/listDetail";
	}

	@GetMapping("/uploadPictureForm")
	public String uploadPictureForm(Principal principal, Model model) {
		User user = userService.findUserByUsername(principal.getName());

		model.addAttribute("userDetail", user.getUserDetail());

		return "userdetails/user-photo";
	}

	@PostMapping("/uploadPicture")
	public String uploadPicture(Model model, Principal principal, @RequestParam("image") MultipartFile multipartFile)
			throws IOException {

		User user = userService.findUserByUsername(principal.getName());

		model.addAttribute("userDetail", user.getUserDetail());

		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

		if (!(FileUploadUtil.getFileExtension(fileName).equals(".jpg")
				|| FileUploadUtil.getFileExtension(fileName).equals(".png"))) {
			model.addAttribute("imageError", "Invalid format, jpg and png allowed.");
			return "userdetails/user-photo";

		}

		if (multipartFile.getSize() > 8000000) {
			model.addAttribute("imageError", "Image too big, maximum 8 MB allowed.");
			return "userdetails/user-photo";
		}

		String uploadDir = "user-photos/";

		File deleteFile;

		if (FileUploadUtil.getFileExtension(fileName).equals(".png")) {
			deleteFile = new File(uploadDir + user.getId() + ".jpg");
			Files.deleteIfExists(deleteFile.toPath());
		} else {
			deleteFile = new File(uploadDir + user.getId() + ".png");
			Files.deleteIfExists(deleteFile.toPath());
		}

		String newFileName = user.getId() + FileUploadUtil.getFileExtension(fileName);

		user.getUserDetail().setImage(newFileName);

		userService.updateUser(user);

		FileUploadUtil.saveFile(uploadDir, newFileName, multipartFile);

		return "redirect:/userdata/uploadPictureForm";
	}

}
