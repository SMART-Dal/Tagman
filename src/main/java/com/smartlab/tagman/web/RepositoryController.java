package com.smartlab.tagman.web;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.smartlab.tagman.service.ImportService;
import com.smartlab.tagman.service.RepositoryService;
import com.smartlab.tagman.service.SecurityService;
import com.smartlab.tagman.util.TagmanUtil;

@Controller
public class RepositoryController {

	@Autowired
	private SecurityService securityService;

	@Autowired
	private ImportService importService;

	@Autowired
	private RepositoryService repositoryService;

	@GetMapping("/import-csv")

	public String importCSV(Model model, String error, String logout) throws IOException, InterruptedException {
		if (!securityService.isAuthenticated()) {
			return "redirect:/login";
		}
		TagmanUtil.runDesignite(TagmanUtil.getBaseFile(),
				"/Users/himesh/Library/CloudStorage/OneDrive-DalhousieUniversity/Thesis/Designite/DesigniteJava.jar",
				0);
		if (error != null)
			model.addAttribute("error", "Your folder does not contain valid values");

		if (logout != null)
			model.addAttribute("message", "You have been logged out successfully.");

		return "redirect:/welcome";
	}

	@PostMapping("/import-csv-design")

	public String uploadDesignFile(Model model, @RequestParam("file") MultipartFile file, RedirectAttributes attributes)
			throws Exception {

		if (file.isEmpty()) {
			attributes.addFlashAttribute("message", "Please select a file to upload.");
			return "redirect:/import-folder";
		}

		repositoryService.storeRepository(file,true);

		return "redirect:/welcome";
	}

	@PostMapping("/import-csv-file")

	public String uploadFile(Model model, @RequestParam("file") MultipartFile file, RedirectAttributes attributes)
			throws Exception {

		if (file.isEmpty()) {
			attributes.addFlashAttribute("message", "Please select a file to upload.");
			return "redirect:/import-folder";
		}

		repositoryService.storeRepository(file,false);

		return "redirect:/welcome";
	}

	@GetMapping("/import-smells2")

	public String importSmells(Model model, String error, String logout) {
		if (!securityService.isAuthenticated()) {
			return "redirect:/login";
		}

		if (error != null)
			model.addAttribute("error", "Your file does not contain valid values");

		if (logout != null)
			model.addAttribute("message", "You have been logged out successfully.");

		return "importSmells";
	}

	@PostMapping("/import-smells2")

	public String uploadSmellsFile(Model model, @RequestParam("file") MultipartFile file, RedirectAttributes attributes)
			throws IOException {

		if (file.isEmpty()) {
			attributes.addFlashAttribute("message", "Please select a file to upload.");
			return "redirect:/";
		}

		try {
			importService.importSmellsFile(file);
		} catch (Exception e) {
			System.out.println("Exception");
			e.printStackTrace();
			return "redirect:/";
		}
		// return success response
		attributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + '!');

		return "redirect:/importSmells";

	}

	public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
		File destFile = new File(destinationDir, zipEntry.getName());

		String destDirPath = destinationDir.getCanonicalPath();
		String destFilePath = destFile.getCanonicalPath();

		if (!destFilePath.startsWith(destDirPath + File.separator)) {
			throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
		}

		return destFile;
	}

}
