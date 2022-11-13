package com.smartlab.tagman.web;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class WellKnown {

	@GetMapping("/.well-known/pki-validation/FCF2CCF2D5BC71A4BB24BF5814E6A65B.txt")
	public void wellKnown(String wellKnown, HttpServletResponse response) throws IOException {
		System.out.println("called");
		String myString = "F7215268DD8EAA83FB3FFD7F30722D357D1FC58E6F65E6D9E035B7D9E01A7991\r\n" + "comodoca.com\r\n"
				+ "a92004f2861c53d";
		response.setContentType("text/plain");
		response.setHeader("Content-Disposition", "attachment;filename=FCF2CCF2D5BC71A4BB24BF5814E6A65B.txt");
		ServletOutputStream out = response.getOutputStream();
		out.println(myString);
		out.flush();
		out.close();
	}
}