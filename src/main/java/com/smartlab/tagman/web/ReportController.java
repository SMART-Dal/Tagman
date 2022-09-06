package com.smartlab.tagman.web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.smartlab.tagman.model.User;
import com.smartlab.tagman.service.AnnotationService;
import com.smartlab.tagman.service.UserService;

@Controller
public class ReportController {

	@Autowired
	UserService userService;

	@Autowired
	AnnotationService annotationService;

	@GetMapping("/report/{bannerId}")
	public void downloadResource(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("bannerId") String bannerId) throws IOException {

		File file = File.createTempFile("report" + bannerId, ".txt");
		FileWriter myWriter = new FileWriter(file);

		writeToFile(bannerId, myWriter);
		myWriter.close();
		if (file.exists()) {

			// get the mimetype
			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			if (mimeType == null) {
				// unknown mimetype so set the mimetype to application/octet-stream
				mimeType = "application/octet-stream";
			}

			response.setContentType(mimeType);

			/**
			 * In a regular HTTP response, the Content-Disposition response header is a
			 * header indicating if the content is expected to be displayed inline in the
			 * browser, that is, as a Web page or as part of a Web page, or as an
			 * attachment, that is downloaded and saved locally.
			 * 
			 */

			/**
			 * Here we have mentioned it to show inline
			 */
			response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));

			// Here we have mentioned it to show as attachment
			// response.setHeader("Content-Disposition", String.format("attachment;
			// filename=\"" + file.getName() + "\""));

			response.setContentLength((int) file.length());

			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

			FileCopyUtils.copy(inputStream, response.getOutputStream());
		}
		file.deleteOnExit();
	}

	private void writeToFile(String bannerId, FileWriter myWriter) throws IOException {

		User user = userService.findByBannerId(bannerId);
		myWriter.write("Student Banner Id : " + user.getBannerId() + "\n");
		myWriter.write("Attempts made correctly : " + user.getSamplesAnswered() + "\n");
		myWriter.write("Total attempts made: " + annotationService.getAllAnnotationsForUser(user.getId()).size() + "\n");
	}
}
