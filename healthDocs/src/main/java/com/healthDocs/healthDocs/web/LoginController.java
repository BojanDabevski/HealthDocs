package com.healthDocs.healthDocs.web;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/*@Controller
@RequestMapping("/login")*/
public class LoginController {

    @GetMapping
    public String getLoginPage(Model model) {
        model.addAttribute("bodyContent","login");
        return "najava-doktor";
    }

}