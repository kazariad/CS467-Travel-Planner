package edu.oregonstate.cs467.travelplanner.web;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import edu.oregonstate.cs467.travelplanner.experience.service.ExperienceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        Map<Long, String> ratingMap = new HashMap<>();
        for (Experience experience : experiences) {
            if (experience.getRatingCnt() > 0) {
                String rating = String.format("%.1f / 5.0",
                        (double) experience.getRatingSum() / (double) experience.getRatingCnt());
                ratingMap.put(experience.getExperienceId(), rating);
            }
        }
        model.addAttribute("ratingMap", ratingMap);

        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
