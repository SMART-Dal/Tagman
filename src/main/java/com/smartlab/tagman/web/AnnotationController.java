package com.smartlab.tagman.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartlab.tagman.model.Annotation;
import com.smartlab.tagman.model.AnnotationReturnModel;
import com.smartlab.tagman.model.DesigniteCSV;
import com.smartlab.tagman.model.DesigniteCSVMethod;
import com.smartlab.tagman.model.NonAdminHomePageModel;
import com.smartlab.tagman.model.Sample;
import com.smartlab.tagman.service.AnnotationService;
import com.smartlab.tagman.service.DesigniteService;
import com.smartlab.tagman.service.ImportService;
import com.smartlab.tagman.service.UserService;
import com.smartlab.tagman.util.TagmanUtil;

@Controller
public class AnnotationController {

	@Autowired
	AnnotationService annotationService;

	@Autowired
	ImportService importService;

	@Autowired
	UserService userService;

	@Autowired
	DesigniteService designiteService;

	@GetMapping("/annotate")
	public String annotate(Model model, String error, String logout, Authentication auth) throws IOException {
		UserDetails loggedInUser = (UserDetails) auth.getPrincipal();
		System.out.println("In annotation controller");
		AnnotationReturnModel randomModel = annotationService
				.getAnnotationForUser(loggedInUser.getUsername().toString(), "1", false, null);

		Sample randomSample = randomModel.getSample();
		// String content = Files.toString(new File("file.txt"), Charsets.UTF_8);
		System.out.println("Sample returned :" + randomSample);
		if (randomSample != null) {
			if (randomSample.getIsClass()) {
				System.out.println("Sample is class");
				String packageName = randomSample.getPathToFile().substring(
						TagmanUtil.ordinalIndexOf(randomSample.getPathToFile(), File.separator, 4) + 1,
						TagmanUtil.ordinalIndexOf(randomSample.getPathToFile(), File.separator, 5));
				String className = randomSample.getPathToFile().substring(
						TagmanUtil.ordinalIndexOf(randomSample.getPathToFile(), File.separator, 5) + 1,
						randomSample.getPathToFile().length() - 5);

				List<DesigniteCSV> desginEntry = designiteService.getdesigniteEntries(packageName, className,
						randomSample.getProjectName());
				if (desginEntry.size() > 0) {
					System.out.println("Query returned more than zero results");
					System.out.println("Entry put into model: " + desginEntry.get(0).toString());
					model.addAttribute("designEntry", desginEntry.get(0));
				}

			} else {
				System.out.println("Sample is method");

				String packageName = randomSample.getPathToFile().substring(
						TagmanUtil.ordinalIndexOf(randomSample.getPathToFile(), File.separator, 4) + 1,
						TagmanUtil.ordinalIndexOf(randomSample.getPathToFile(), File.separator, 5));
				String className = randomSample.getPathToFile().substring(
						TagmanUtil.ordinalIndexOf(randomSample.getPathToFile(), File.separator, 5) + 1,
						TagmanUtil.ordinalIndexOf(randomSample.getPathToFile(), File.separator, 6));
				String methodName = randomSample.getPathToFile().substring(
						TagmanUtil.ordinalIndexOf(randomSample.getPathToFile(), File.separator, 6) + 1,
						randomSample.getPathToFile().length() - 5);

				List<DesigniteCSVMethod> desginEntry = designiteService.getdesigniteMethodEntries(packageName,
						className, randomSample.getProjectName(), methodName);

				if (desginEntry.size() > 0) {
					System.out.println("Query returned more than zero results");
					System.out.println("Entry put into model: " + desginEntry.get(0).toString());

					// System.out.println("entry" + desginEntry.get(0).toString());
					model.addAttribute("designEntryMethod", desginEntry.get(0));
				}
			}
//			String packageName = randomSample.getPathToFile().substring(
//					TagmanUtil.ordinalIndexOf(randomSample.getPathToFile(), File.separator, 4),
//					TagmanUtil.ordinalIndexOf(randomSample.getPathToFile(), File.separator, 5));
//			String className = randomSample.getPathToFile()
//					.substring(TagmanUtil.ordinalIndexOf(randomSample.getPathToFile(), File.separator, 5));

//			List<DesigniteCSV> desginEntry = designiteService.getdesigniteEntries(packageName, className,
//					randomSample.getProjectName());
//			if (desginEntry.size() > 0)
//				System.out.println("entry" + desginEntry.get(0).toString());
			List<String> files = TagmanUtil.readFile(randomSample.getPathToFile());
			StringBuilder sb = new StringBuilder();
			files.forEach(file -> {
				sb.append(file).append("\n");
			});
			model.addAttribute("isClass", randomSample.getIsClass());

			model.addAttribute("sample", sb.toString());
			model.addAttribute("id", randomSample.getId());
			model.addAttribute("inputObject", new NonAdminHomePageModel());
			model.addAttribute("isEmpty", "0");
			model.addAttribute("filename", randomSample.getPathToFile()
					.substring(randomSample.getPathToFile().lastIndexOf(File.separator) + 1));
			System.out.println(TagmanUtil.readFile(randomSample.getPathToFile()).toString());

			List<String> smells = List.of(randomSample.getSmells().split(","));
			List<String> smellsConv = new ArrayList<>();

			smells.stream().forEach(smell -> {
				if (smell.equals("1"))
					smellsConv.add("Complex Method");
				else if (smell.equals("2"))
					smellsConv.add("Long Method");
				else if (smell.equals("3"))
					smellsConv.add("MultiFaceted Abstraction");

			});
			smellsConv.forEach(System.out::println);

			model.addAttribute("smellsSent", smellsConv);

		} else {
			model.addAttribute("sample", "");
			model.addAttribute("id", "");
			model.addAttribute("filename", "");
			model.addAttribute("inputObject", new NonAdminHomePageModel());
			model.addAttribute("isEmpty", "1");
			model.addAttribute("isClass", false);

		}
		model.addAttribute("totalUserCount", randomModel.getSize());

		return "indexNonAdmin";
	}

	@PostMapping("/annotate")
	public String annotatePost(Model model, @RequestParam("sampleId") String sampleId,
			@RequestParam(value = "cers", required = false) String cers,
			@RequestParam(value = "EmptyCB", required = false) String checkbox,
			@RequestParam(value = "MultiAB", required = false) String checkbox2,
			@RequestParam(value = "ComplexME", required = false) String checkbox3,
			@RequestParam(value = "LongPM", required = false) String checkbox4, String error, String logout,
			Authentication auth) throws IOException {
		// User loggedInUser = (User) auth.getPrincipal();
		Long userId = userService.findByUsername(((UserDetails) auth.getPrincipal()).getUsername()).getId();
		UserDetails loggedInUser = (UserDetails) auth.getPrincipal();

		Annotation annotation = new Annotation();
		String smells = "";
		boolean oneSmellDetected = false;

		System.out.println("cers" + cers);
		if (cers != null) {
			if (cers.toLowerCase().contains("complex method")) {
				annotation.setCM(true);
				oneSmellDetected = true;

			} else {
				annotation.setCM(false);
			}
			if (cers.toLowerCase().contains("long method")) {
				annotation.setLP(true);
				oneSmellDetected = true;

			} else {
				annotation.setLP(false);
			}
			if (cers.toLowerCase().contains("multiFaceted abstraction")) {
				annotation.setMA(true);
				oneSmellDetected = true;

			} else {
				annotation.setMA(false);
			}
		}

//		if (smells.length() > 1)
//			smells = smells.substring(0, smells.length() - 1);

		annotationService.getAnnotationForUser(loggedInUser.getUsername(), "", true, Long.parseLong(sampleId));
		Sample sampleCollected = annotationService.getSampleById(Long.parseLong(sampleId));
		// if (checkbox == null) {
//			annotation.setECB(true);
//		} else {
//			annotation.setECB(false);
//			oneSmellDetected = true;
//		}
//
//		if (checkbox2 == null) {
//			annotation.setMA(true);
//		} else {
//			annotation.setMA(false);
//			oneSmellDetected = true;
//
//		}
//		if (checkbox3 == null) {
//			annotation.setCM(true);
//		} else {
//			annotation.setCM(false);
//			oneSmellDetected = true;
//
//		}
//		if (checkbox4 == null) {
//			annotation.setLP(true);
//
//		} else {
//			annotation.setLP(false);
//			oneSmellDetected = true;
//
//		}

		annotation.setSampleId(sampleCollected.getId());
		annotation.setUserId(userId);
		annotation.setSmellId(null);
		annotation.setSmell(oneSmellDetected);
		annotation.setCreationDateTime(new Date());
//		model.addAttribute("isClass", .getIsClass());

		Annotation annotationRet = annotationService.saveAnnotation(annotation);
		// userService.addEntryForUser(userId, userService.getCountForUser(userId)+1);

		return "redirect:/annotate";
	}

}
