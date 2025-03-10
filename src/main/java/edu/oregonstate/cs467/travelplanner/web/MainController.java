package edu.oregonstate.cs467.travelplanner.web;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import edu.oregonstate.cs467.travelplanner.experience.service.ExperienceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {
    private final ExperienceService experienceService;
    private final String gmapsApiKey;

    public MainController(
            ExperienceService experienceService,
            @Value("${google.maps.api.key}")
            String gmapsApiKey
    ) {
        this.experienceService = experienceService;
        this.gmapsApiKey = gmapsApiKey;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("gmapsApiKey", gmapsApiKey);
        List<Experience> experiences = experienceService.getFeaturedExperiences();
        model.addAttribute("experiences", experiences);
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
