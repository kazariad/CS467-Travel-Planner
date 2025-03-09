package edu.oregonstate.cs467.travelplanner.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    private final String gmapsApiKey;

    public MainController(@Value("${google.maps.api.key}") String gmapsApiKey) {
        this.gmapsApiKey = gmapsApiKey;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("gmapsApiKey", gmapsApiKey);
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
