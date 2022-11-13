package com.smartlab.tagman.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.smartlab.tagman.model.InstructorMarking;
import com.smartlab.tagman.model.InstructorsHomePageModel;
import com.smartlab.tagman.model.User;
import com.smartlab.tagman.repository.InstructorRepository;
import com.smartlab.tagman.service.SecurityService;
import com.smartlab.tagman.service.UserService;

@Controller
public class InstructorController {

	@Autowired
	InstructorRepository repository;

	@Autowired
	SecurityService securityService;

	@Autowired
	UserService userService;

	@PostMapping("/instruct")
	public String annotate(HttpServletResponse response, HttpServletRequest request,
			@ModelAttribute("instructForm") InstructorsHomePageModel userForm, Model model, Authentication auth)
			throws IOException {
		System.out.println("In controller");
		InstructorMarking marking = new InstructorMarking();
		System.out.println("annottatiton" + userForm.getAnnotationId());
		// System.out.println(userForm.);
//		if (userForm.getAnnotation() == null)
//			return "redirect:";
		if(userForm.getAnnotationId().equals(""))
			userForm.setAnnotationId("0");
		marking.setAnnotationId(Long.parseLong(userForm.getAnnotationId()));
		User loggedIn = securityService.getDetails();
		System.out.println("LoggedIn.getId" + loggedIn.getId());
		// request.getSession().setAttribute("userIdForSafety", loggedIn.getId());
		System.out.println("userForm"+userForm.getBannerId());
		
		User user = new User();
	//	marking.setUserId(userService.findByBannerId(userForm.getBannerId()).getId());

		marking.setMarkerId(loggedIn.getId());
		marking.setMarkedCorrect(userForm.getIsMarkedCorrect().equals("1") ? true : false);
		repository.save(marking);
		// userService.getCountForUser(null);
		if (userForm.getIsMarkedCorrect().equals("1")) {
			userService.updateUserCount(new Long(user.getSamplesAnswered() + 1), user.getId());
			if (user.getSamplesAnswered() >= 24) {
				request.getSession().setAttribute("maxAnnotations", true);
			} else {
				request.getSession().setAttribute("maxAnnotations", false);
			}
		} else {
			request.getSession().setAttribute("maxAnnotations", false);
		}
		System.out.println(userForm.toString());
//		request.getSession().setAttribute("userBanner",
//				userService.findByBannerId(userForm.getBannerId()).getBannerId());
		request.getSession().setAttribute("introduceSample", true);
		request.getSession().setAttribute("userIdForSafety", loggedIn.getId());
		response.setStatus(302);
		return "redirect:";
	}
}
