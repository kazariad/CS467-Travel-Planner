package edu.oregonstate.cs467.travelplanner.web.dto;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/viewUser")
    public String viewUser() {
        return "viewUser";
    }
}
