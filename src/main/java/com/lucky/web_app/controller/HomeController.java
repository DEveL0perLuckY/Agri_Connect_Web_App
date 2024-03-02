package com.lucky.web_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String getHome() {
        return "Home/home";
    }

    @GetMapping("/features")
    public String getFeatures() {
        return "Home/features";
    }

    @GetMapping("/about")
    public String getAbout() {
        return "Home/about";
    }

    @GetMapping("/contact")
    public String getContact() {
        return "Home/contact";
    }

}
