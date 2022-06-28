package com.healthDocs.healthDocs.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {
    @GetMapping
    public String getIndex(){
        return "pochetna";
    }

    @GetMapping(value="/about")
    public String about(){
        return "zaNas";
    }
}