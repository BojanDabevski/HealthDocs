package com.healthDocs.healthDocs.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
/*@Controller
@RequestMapping("/login")*/
public class LoginController {

    @GetMapping
    public String getLoginPage(Model model) {
        model.addAttribute("bodyContent","login");
        return "najava-doktor";
    }

}