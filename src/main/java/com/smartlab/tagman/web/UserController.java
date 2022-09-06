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

	@GetMapping({ "/", "/welcome" })
	public String welcome(HttpServletRequest request, Model model) throws IOException {
		System.out.println("In get method mapping");
		User loggedIn = securityService.getDetails();
		if (!securityService.isAuthenticated()) {

			return "login-new";
		} else if (loggedIn.isAdmin) {
			model.addAttribute("user", loggedIn);
			return "index";
		} else if (loggedIn.isInstructor) {
			model.addAttribute("user", loggedIn);
			List<User> users = userService.findAllUsers();
			// getUserDetails(model, userLists);
			model.addAttribute("userList", users);
			// users.forEach(user -> System.out.println(user.getUsername()));
			model.addAttribute("instructForm", new InstructorsHomePageModel());

			if ((request.getSession().getAttribute("introduceSample") != null)
					&& (boolean) request.getSession().getAttribute("introduceSample")) {
				System.out.println("session sample not null");
				// request.getSession().removeAttribute("introduceSample");
				if ((boolean) request.getSession().getAttribute("maxAnnotations")) {
					model.addAttribute("maxAnnotations", "1");
					return "indexInstructor";
				}
				String userId = ((Long) request.getSession().getAttribute("userIdForSafety")).toString();
				System.out.println("UserId :" + userId);
				List<User> userLists = new ArrayList<>();
				userLists.add(userService.findOne(userId));
				String userBanner = (String) request.getSession().getAttribute("userBanner");
				if (userBanner != null) {
					users = userService.findFittingBannerId(userBanner.substring(0, userBanner.length() - 1));
					// System.out.println(userForm.getBannerId().substring(0,
					// userForm.getBannerId().length() - 2));
					model.addAttribute("userListIns", users);
				}
				getUserDetails(model, users);
			}

			return "indexInstructor";
		}

		AnnotationReturnModel modelReturn = annotationService.getAnnotationForUser(loggedIn.getUsername().toString(),
				"1", false, null);
		// String content = Files.toString(new File("file.txt"), Charsets.UTF_8);
		Sample randomSample = modelReturn.getSample();

		if (randomSample.getIsClass()) {
			String packageName = randomSample.getPathToFile().substring(
					TagmanUtil.ordinalIndexOf(randomSample.getPathToFile(), "/", 4) + 1,
					TagmanUtil.ordinalIndexOf(randomSample.getPathToFile(), "/", 5));
			String className = randomSample.getPathToFile().substring(
					TagmanUtil.ordinalIndexOf(randomSample.getPathToFile(), "/", 5) + 1,
					randomSample.getPathToFile().length() - 5);

			List<DesigniteCSV> desginEntry = designiteService.getdesigniteEntries(packageName, className,
					randomSample.getProjectName());
			if (desginEntry.size() > 0) {
				System.out.println("entry" + desginEntry.get(0).toString());
				model.addAttribute("designEntry", desginEntry.get(0));
			}
		} else {
			String packageName = randomSample.getPathToFile().substring(
					TagmanUtil.ordinalIndexOf(randomSample.getPathToFile(), "/", 4) + 1,
					TagmanUtil.ordinalIndexOf(randomSample.getPathToFile(), "/", 5));
			String className = randomSample.getPathToFile().substring(
					TagmanUtil.ordinalIndexOf(randomSample.getPathToFile(), "/", 5) + 1,
					TagmanUtil.ordinalIndexOf(randomSample.getPathToFile(), "/", 6));
			String methodName = randomSample.getPathToFile().substring(
					TagmanUtil.ordinalIndexOf(randomSample.getPathToFile(), "/", 6) + 1,
					randomSample.getPathToFile().length() - 5);

			List<DesigniteCSVMethod> desginEntry = designiteService.getdesigniteMethodEntries(packageName, className,
					randomSample.getProjectName(), methodName);

			if (desginEntry.size() > 0) {
				// System.out.println("entry" + desginEntry.get(0).toString());
				model.addAttribute("designEntryMethod", desginEntry.get(0));
			}
		}
		if (randomSample != null) {
			List<String> files = TagmanUtil.readFile(randomSample.getPathToFile());
			StringBuilder sb = new StringBuilder();
			files.forEach(file -> {
				sb.append(file).append("\n");
			});
			model.addAttribute("isClass", randomSample.getIsClass());
			model.addAttribute("sample", sb.toString());
			model.addAttribute("id", randomSample.getId());
			model.addAttribute("filename",
					randomSample.getPathToFile().substring(randomSample.getPathToFile().lastIndexOf("/") + 1));
			// System.out.println(TagmanUtil.readFile(randomSample.getPathToFile()).toString());
			model.addAttribute("user", loggedIn);
			model.addAttribute("isEmpty", "0");

//		
//		importService.processDesignFile(new File(
//				"/Users/himesh/Library/CloudStorage/OneDrive-DalhousieUniversity/Thesis/Mootex/run2/eval/designite_out_java/adikul30_MaterialNews/TypeMetrics.csv"),
//				false);
		} else {
			System.out.println("Sample returned nulll");
			model.addAttribute("isEmpty", "1");

		}
		model.addAttribute("totalUserCount", modelReturn.getSize());
		return "indexNonAdmin";
	}

	@PostMapping({ "/", "/welcome" })
	public String welcomePost(HttpServletRequest request,
			@ModelAttribute("instructForm") InstructorsHomePageModel userForm, Model model) throws IOException {
		// request.getAttributeNames().asIterator().forEachRemaining(str ->
		// System.out.println("str" + str));

		User loggedIn = securityService.getDetails();
		if (!securityService.isAuthenticated()) {

			return "login-new";
		}

		else if (loggedIn.isInstructor) {
			model.addAttribute("isRedirect", "isRedirect");
			// System.out.println("is user" + userForm.getIsUserSubm().equals("true"));
			if (userForm.getIsUserSubm().equals("true")) {
				if (userForm.getBannerId().length() == 0)
					return "indexInstructor";
				List<User> users = userService
						.findFittingBannerId(userForm.getBannerId().substring(0, userForm.getBannerId().length() - 1));
				// System.out.println(userForm.getBannerId().substring(0,
				// userForm.getBannerId().length() - 2));
				model.addAttribute("userListIns", users);
				if (users.size() == 1) {
					getUserDetails(model, users);

				}
			} else {
				model.addAttribute("isRedirect", "isRedirect");
				// System.out.println("is user" + userForm.getIsUserSubm().equals("false"));
				// System.out.println("user details ::" + userForm.toString());

			}
			// userForm.setBannerId("");
			return "indexInstructor";
		}

		return "redirect:/";
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
		}
		else {
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

	@GetMapping("/instructions")
	public String returnInst() {
		return "instructions";
	}
}
