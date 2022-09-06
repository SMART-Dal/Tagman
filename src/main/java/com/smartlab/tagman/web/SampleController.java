package com.smartlab.tagman.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smartlab.tagman.model.Sample;
import com.smartlab.tagman.model.User;
import com.smartlab.tagman.service.ImportService;
import com.smartlab.tagman.service.SecurityService;
import com.smartlab.tagman.service.UserService;

@Controller
public class SampleController {

    @Autowired
    private SecurityService securityService;


    @Autowired
    ImportService importService;
    
    @GetMapping("/getAllSamples")
    public String registration(Model model,RedirectAttributes attributes) {
        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

     List<Sample> allSamples = importService.getAllSamples();
     System.out.println("Samples"+allSamples.size());
     model.addAttribute("samples",allSamples);
     
     return "showsamples";
    }


}
