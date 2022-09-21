package com.smartlab.tagman.web;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.weaver.IHasPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartlab.tagman.model.Annotation;
import com.smartlab.tagman.model.AnnotationReturnModel;
import com.smartlab.tagman.model.DesigniteCSV;
import com.smartlab.tagman.model.DesigniteCSVMethod;
import com.smartlab.tagman.model.InstructorMarking;
import com.smartlab.tagman.model.InstructorsHomePageModel;
import com.smartlab.tagman.model.Sample;
import com.smartlab.tagman.model.User;
import com.smartlab.tagman.service.AnnotationService;
import com.smartlab.tagman.service.DesigniteService;
import com.smartlab.tagman.service.ImportService;
import com.smartlab.tagman.service.SecurityService;
import com.smartlab.tagman.service.UserService;
import com.smartlab.tagman.util.TagmanUtil;

@Controller
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private UserValidator userValidator;

	@Value("${registration.allowed}")
	private String regAllowed;

	@Autowired
	AnnotationService annotationService;

	@Autowired
	DesigniteService designiteService;

	@Autowired
	ImportService importService;

	@GetMapping("/registration")
	public String registration(Model model) {

		if (securityService.isAuthenticated()) {
			return "redirect:/";
		}
		if (!regAllowed.equalsIgnoreCase("true"))

			return "regnotallowed";

		model.addAttribute("userForm", new User());
		return "signup-new";
	}

	@PostMapping("/registration")
	public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult) {
		userValidator.validate(userForm, bindingResult);
		userForm.setAdmin(false);
		if (!regAllowed.equalsIgnoreCase("true"))

			return "regnotallowed";

		if (bindingResult.hasErrors()) {
			return "signup-new";
		}

		userService.save(userForm);

		securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());

		return "redirect:/welcome";
	}

	@GetMapping("/login")
	public String login(Model model, String error, String logout) {
		if (securityService.isAuthenticated()) {
			return "redirect:/";
		}

		if (error != null)
			model.addAttribute("error", "Your username and password is invalid.");

		if (logout != null)
			model.addAttribute("message", "You have been logged out successfully.");

		return "login-new";
	}

	/**
	 * @param model
	 * @param users
	 * @throws IOException
	 */
	private void getUserDetails(Model model, List<User> users) throws IOException {
		System.out.println("In getUserDetails");
		if (users.get(0).getSamplesAnswered() >= 25) {
			model.addAttribute("maxAnnotations", "1");

			return;
		} else {
			model.addAttribute("maxAnnotations", "0");
		}

		InstructorMarking annotatedUser = annotationService.getUnmarkedAnnotation(users.get(0).getId());
		if (annotatedUser == null) {
			System.out.println("Annotated user returned null");
			model.addAttribute("userAnnotationsZero", "1");
			return;
		}
		Annotation annotation = annotationService.getAnnotationById(annotatedUser.getAnnotationId());
		Sample sample = annotationService.getSampleById(annotation.getSampleId());
		model.addAttribute("bannerId", users.get(0).getBannerId());
		// System.out.println("Added BannerId +"+users.get(0).getBannerId());
		List<String> files = TagmanUtil.readFile(sample.getPathToFile());
		StringBuilder sb = new StringBuilder();
		files.forEach(file -> {
			sb.append(file).append("\n");
		});
		model.addAttribute("annotationId", annotation.getId());
		model.addAttribute("sample", sb.toString());

		List<String> smells = new ArrayList<>();
		if (!annotation.isCM()) {
			smells.add("Complex Method");
		}

		if (!annotation.isECB()) {
			smells.add("Empty Catch Block");
		}
		if (!annotation.isLP()) {
			smells.add("Long Parameter");
		}
		if (!annotation.isMA()) {
			smells.add("Mutlifaceted Abstraction");
		}
		if (smells.size() > 0)
			model.addAttribute("smells", smells);
	}

}
