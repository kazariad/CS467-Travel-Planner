package edu.oregonstate.cs467.travelplanner.experience.controller;

import edu.oregonstate.cs467.travelplanner.experience.dto.CreateUpdateExperienceDto;
import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import edu.oregonstate.cs467.travelplanner.experience.service.ExperienceService;
import edu.oregonstate.cs467.travelplanner.user.model.User;
import edu.oregonstate.cs467.travelplanner.user.service.UserService;
import edu.oregonstate.cs467.travelplanner.util.TimeUtils;
import edu.oregonstate.cs467.travelplanner.util.security.AuthenticatedUserProvider;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.time.Instant;

@Controller
@RequestMapping("/experience")
public class ExperienceWebController {
    private final AuthenticatedUserProvider authUserProvider;
    private final ExperienceService experienceService;
    private final UserService userService;
    private final TimeUtils timeUtils;
    private final String gmapsApiKey;

    public ExperienceWebController(
            AuthenticatedUserProvider authUserProvider,
            ExperienceService experienceService,
            UserService userService,
            TimeUtils timeUtils,
            @Value("${google.maps.api.key}")
            String gmapsApiKey
    ) {
        this.authUserProvider = authUserProvider;
        this.experienceService = experienceService;
        this.userService = userService;
        this.timeUtils = timeUtils;
        this.gmapsApiKey = gmapsApiKey;
    }

    @GetMapping("/{experienceId}")
    public String viewExperience(@PathVariable long experienceId, Model model) {
        Experience experience = experienceService.getExperience(experienceId);
        if (experience == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        model.addAttribute("experience", experience);

        User author = userService.findById(experience.getUserId()).get();
        model.addAttribute("author", author.getUsername());
        model.addAttribute("submittedDuration", timeUtils.formatDuration(Duration.between(experience.getCreatedAt(), Instant.now())));

        if (experience.getRatingCnt() > 0) {
            model.addAttribute("rating", String.format("%.1f / 5.0", (double) experience.getRatingSum() / (double) experience.getRatingCnt()));
        }

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
            model.addAttribute("location", String.format("%.6f, %.6f", experience.getLocationLat(), experience.getLocationLng()));
            UriBuilder mapUrlBuilder = UriComponentsBuilder
                    .fromUriString("https://www.google.com/maps/embed/v1/")
                    .path("view")
                    .queryParam("key", gmapsApiKey)
                    .queryParam("center", String.format("%s,%s", experience.getLocationLat(), experience.getLocationLng()))
                    .queryParam("zoom", 19);
            model.addAttribute("mapUrl", mapUrlBuilder.build());
        }

        UriBuilder streetViewUrlBuilder = UriComponentsBuilder
                .fromUriString("https://www.google.com/maps/embed/v1/")
                .path("streetview")
                .queryParam("key", gmapsApiKey)
                .queryParam("location", String.format("%s,%s", experience.getLocationLat(), experience.getLocationLng()))
                .queryParam("fov", 90);
        model.addAttribute("streetViewUrl", streetViewUrlBuilder.build());

        return "experience/view-experience";
    }

    @GetMapping(path = "/create")
    public String initCreateForm(Model model) {
        if (!authUserProvider.isAnyUser()) throw new AccessDeniedException("Access denied");

        CreateUpdateExperienceDto experienceDto = new CreateUpdateExperienceDto();
        // temporary dummy values until GMaps integration
        experienceDto.setAddress("Atlantis");
        experienceDto.setLocationLat(0.0);
        experienceDto.setLocationLng(0.0);
        model.addAttribute("experienceDto", experienceDto);
        return "experience/create-experience";
    }

    @PostMapping(path = "/create")
    public String createExperience(
            @Valid @ModelAttribute("experienceDto") CreateUpdateExperienceDto experienceDto,
            BindingResult bindingResult)
    {
        if (bindingResult.hasErrors()) {
            return "experience/create-experience";
        } else {
            long experienceId = experienceService.createExperience(experienceDto);
            return "redirect:/experience/" + experienceId;
        }
    }

    @GetMapping(path = "/{experienceId}/update")
    public String initUpdateForm(@PathVariable long experienceId, Model model) {
        Experience experience = experienceService.getExperience(experienceId);
        if (experience == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        if (!authUserProvider.isUserWithId(experience.getUserId())) throw new AccessDeniedException("Access denied");

        CreateUpdateExperienceDto experienceDto = new CreateUpdateExperienceDto(experience);
        model.addAttribute("experienceId", experienceId);
        model.addAttribute("experienceDto", experienceDto);
        return "experience/update-experience";
    }

    @PostMapping(path = "/{experienceId}/update")
    public String updateExperience(
            @PathVariable long experienceId,
            @Valid @ModelAttribute("experienceDto") CreateUpdateExperienceDto experienceDto,
            BindingResult bindingResult)
    {
        if (bindingResult.hasErrors()) {
            return "experience/update-experience";
        } else {
            experienceService.updateExperience(experienceId, experienceDto);
            return "redirect:/experience/{experienceId}";
        }
    }
}
