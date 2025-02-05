package edu.oregonstate.cs467.travelplanner.experience.controller;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import edu.oregonstate.cs467.travelplanner.experience.model.GeoPoint;
import edu.oregonstate.cs467.travelplanner.experience.service.ExperienceService;
import edu.oregonstate.cs467.travelplanner.user.model.User;
import edu.oregonstate.cs467.travelplanner.user.service.UserService;
import edu.oregonstate.cs467.travelplanner.util.TimeUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.time.Instant;

@Controller
@RequestMapping("/experience")
public class ExperienceWebController {
    private final ExperienceService experienceService;
    private final UserService userService;
    private final TimeUtil timeUtil;
    private final String gmapsApiKey;

    public ExperienceWebController(
            ExperienceService experienceService,
            UserService userService, TimeUtil timeUtil,
            @Value("${google.maps.api.key}")
            String gmapsApiKey
    ) {
        this.experienceService = experienceService;
        this.userService = userService;
        this.timeUtil = timeUtil;
        this.gmapsApiKey = gmapsApiKey;
    }

    @GetMapping("/{experienceId}")
    public String getExperience(@PathVariable long experienceId, Model model) {
        Experience experience = experienceService.getExperience(experienceId);
        model.addAttribute("experience", experience);

        User author = userService.findById(experience.getUserId()).get();
        model.addAttribute("author", author.getUsername());
        model.addAttribute("submittedDuration", timeUtil.coarseDuration(Duration.between(experience.getCreatedAt(), Instant.now())));

        if (experience.getRatingCnt() > 0) {
            model.addAttribute("rating", String.format("%.1f / 5.0", (double) experience.getRatingSum() / (double) experience.getRatingCnt()));
        }

        GeoPoint gp = experience.getLocation();
        if (experience.getAddress() != null) {
            model.addAttribute("location", experience.getAddress());
            UriBuilder mapUrlBuilder = UriComponentsBuilder
                    .fromUriString("https://www.google.com/maps/embed/v1/")
                    .path("place")
                    .queryParam("key", gmapsApiKey)
                    .queryParam("q", experience.getAddress())
                    .queryParam("zoom", 19);
            model.addAttribute("mapUrl", mapUrlBuilder.build());
        } else {
            model.addAttribute("location", String.format("%.6f, %.6f", gp.lat(), gp.lng()));
            UriBuilder mapUrlBuilder = UriComponentsBuilder
                    .fromUriString("https://www.google.com/maps/embed/v1/")
                    .path("view")
                    .queryParam("key", gmapsApiKey)
                    .queryParam("center", String.format("%s,%s", gp.lat(), gp.lng()))
                    .queryParam("zoom", 19);
            model.addAttribute("mapUrl", mapUrlBuilder.build());
        }

        UriBuilder streetViewUrlBuilder = UriComponentsBuilder
                .fromUriString("https://www.google.com/maps/embed/v1/")
                .path("streetview")
                .queryParam("key", gmapsApiKey)
                .queryParam("location", String.format("%s,%s", gp.lat(), gp.lng()))
                .queryParam("fov", 90);
        model.addAttribute("streetViewUrl", streetViewUrlBuilder.build());

        return "experience/view-experience";
    }
}
