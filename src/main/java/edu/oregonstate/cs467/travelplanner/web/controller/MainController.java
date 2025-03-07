package edu.oregonstate.cs467.travelplanner.web.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // changed to test home page

    @GetMapping("/landing")
    public String landing() {
        return "landing";
    }

}
