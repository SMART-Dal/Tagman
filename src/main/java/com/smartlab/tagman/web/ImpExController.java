package com.smartlab.tagman.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smartlab.tagman.service.ImportService;
import com.smartlab.tagman.service.SecurityService;

@Controller
public class ImpExController {

	@Autowired
	private SecurityService securityService;

	@Autowired
	private ImportService importService;

	@GetMapping("/import-folder")

	public String importFolder(Model model, String error, String logout) {
		if (!securityService.isAuthenticated()) {
			return "redirect:/login";
		}

		if (error != null)
			model.addAttribute("error", "Your folder does not contain valid values");

		if (logout != null)
			model.addAttribute("message", "You have been logged out successfully.");

		return "import";
	}

	@PostMapping("/import-folder")

	public String uploadFile(Model model, @RequestParam("file") MultipartFile file,
			@RequestParam("fileDesignite") MultipartFile fileDesignite, RedirectAttributes attributes)
			throws IOException {
		if (file.isEmpty()) {
			attributes.addFlashAttribute("message", "Please select a file to upload.");
			return "redirect:/import-folder";
		}

		System.out.println("File:" + file.getOriginalFilename());
		byte[] buffer = new byte[1024];
		ZipInputStream zis = new ZipInputStream(file.getInputStream());
		ZipEntry zipEntry = zis.getNextEntry();
		File destDir = new File(System.getProperty("user.home") + File.separator + "tagUtilOutput");
		File designDir = new File(System.getProperty("user.home") + File.separator + "designiteOutput");

		while (zipEntry != null) {
			File newFile = newFile(destDir, zipEntry);
			if (zipEntry.isDirectory()) {
				if ((!newFile.isDirectory() && !newFile.mkdirs())) {
					throw new IOException("Failed to create directory " + newFile);
				}
			} else {
				if (!newFile.getName().startsWith(".")) {
					// fix for Windows-created archives
					File parent = newFile.getParentFile();

					if (!parent.isDirectory() && !parent.mkdirs()) {
						throw new IOException("Failed to create directory " + parent);
					}

					// write file content
					FileOutputStream fos = new FileOutputStream(newFile);
					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
					fos.close();

					
				}
				
			}
			zipEntry = zis.getNextEntry();
		}

		zis = new ZipInputStream(fileDesignite.getInputStream());
		zipEntry = zis.getNextEntry();
		while (zipEntry != null) {
			File newFile = newFile(designDir, zipEntry);
			if (zipEntry.isDirectory()) {
				if (!newFile.isDirectory() && !newFile.mkdirs()) {
					throw new IOException("Failed to create directory " + newFile);
				}
			} else {
				// fix for Windows-created archives
				File parent = newFile.getParentFile();
				if (!parent.isDirectory() && !parent.mkdirs()) {
					throw new IOException("Failed to create directory " + parent);
				}

				// write file content
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
			}
			zipEntry = zis.getNextEntry();
		}

		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		// save the file on the local file system
		try {
			importService.importFolder((String) destDir.getAbsolutePath(), (String) designDir.getAbsolutePath(),
					Boolean.getBoolean((String) "true"),true);

		} catch (Exception e) {
			System.out.println("Exception");
			e.printStackTrace();
			return "redirect:/";
		}
		// return success response
		attributes.addFlashAttribute("message", "You successfully uploaded " + fileName + '!');

		return "redirect:/getAllSamples";
	}

	@GetMapping("/import-smells")

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

	@PostMapping("/import-smells")

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
