package edu.oregonstate.cs467.travelplanner.experience.controller;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import edu.oregonstate.cs467.travelplanner.experience.service.ExperienceService;
import edu.oregonstate.cs467.travelplanner.user.model.User;
import edu.oregonstate.cs467.travelplanner.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/experience")
public class ExperienceWebController {
    private final ExperienceService experienceService;
    private final UserService userService;

    public ExperienceWebController(ExperienceService experienceService, UserService userService) {
        this.experienceService = experienceService;
        this.userService = userService;
    }

    @GetMapping("/{experienceId}")
    public String getExperience(@PathVariable long experienceId, Model model) {
        Experience experience = experienceService.getExperience(experienceId);
        User user = userService.findById(experience.getUserId()).get();
        model.addAttribute("experience", experience);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("coordinates", String.format("%.6f, %.6f", experience.getLocation().lat(), experience.getLocation().lng()));
        model.addAttribute("rating", String.format("%.1f / 5.0", (double) experience.getRatingSum() / (double) experience.getRatingCnt()));
        return "experience/ViewExperience";
    }
}
