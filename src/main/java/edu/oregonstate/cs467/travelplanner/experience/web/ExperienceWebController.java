package edu.oregonstate.cs467.travelplanner.experience.web;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import edu.oregonstate.cs467.travelplanner.experience.service.ExperienceService;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.CreateUpdateExperienceDto;
import edu.oregonstate.cs467.travelplanner.experience.web.util.ExperienceHashIdEncoder;
import edu.oregonstate.cs467.travelplanner.experience.web.util.ExperienceUrlGenerator;
import edu.oregonstate.cs467.travelplanner.trip.model.Trip;
import edu.oregonstate.cs467.travelplanner.trip.service.TripService;
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
import java.util.List;

@Controller
@RequestMapping("/experience")
public class ExperienceWebController {
    private final AuthenticatedUserProvider authUserProvider;
    private final ExperienceService experienceService;
    private final ExperienceHashIdEncoder hashIdEncoder;
    private final ExperienceUrlGenerator urlGenerator;
    private final UserService userService;
    private final TimeUtils timeUtils;
    private final String gmapsApiKey;
    private final TripService tripService;

    public ExperienceWebController(
            AuthenticatedUserProvider authUserProvider,
            ExperienceService experienceService,
            ExperienceHashIdEncoder hashIdEncoder,
            ExperienceUrlGenerator urlGenerator,
            UserService userService,
            TimeUtils timeUtils,
            TripService tripService,
            @Value("${google.maps.api.key}")
            String gmapsApiKey
    ) {
        this.authUserProvider = authUserProvider;
        this.experienceService = experienceService;
        this.hashIdEncoder = hashIdEncoder;
        this.urlGenerator = urlGenerator;
        this.userService = userService;
        this.timeUtils = timeUtils;
        this.gmapsApiKey = gmapsApiKey;
        this.tripService = tripService;
    }

    @ModelAttribute
    public void initModel(Model model) {
        model.addAttribute("gmapsApiKey", gmapsApiKey);
    }

    @GetMapping("/{hashId}")
    public String viewExperience(@PathVariable String hashId, Model model) {
        long experienceId = hashIdEncoder.decode(hashId);
        // retrieve the Experience object from persistent storage based on the path ID
        Experience experience = experienceService.getExperience(experienceId);
        // if the Experience doesn't exist (or was deleted), return a 404 error
        if (experience == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        // add the Experience object to the model to make it accessible by the HTML template engine
        model.addAttribute("experience", experience);

        // find the User who created this Experience and add their username to the model
        User author = userService.findById(experience.getUserId()).get();
        model.addAttribute("author", author.getUsername());
        // how long ago this Experience was submitted as a relative duration, e.g. "12 hours ago", "5 days ago", etc.
        // use relative time to avoid having to determine the client's timezone (surprisingly there isn't a standard header for this, requires JS)
        model.addAttribute("submittedDuration", timeUtils.formatDuration(Duration.between(experience.getCreatedAt(), Instant.now())));

        String location = String.format("%s,%s", experience.getLocationLat(), experience.getLocationLng());
        UriBuilder mapUrlBuilder = UriComponentsBuilder
                .fromUriString("https://www.google.com/maps/embed/v1/")
                .path("place")
                .queryParam("key", gmapsApiKey)
                .queryParam("zoom", 19);
        if (experience.getPlaceId() != null) {
            mapUrlBuilder = mapUrlBuilder.queryParam("q", "place_id:" + experience.getPlaceId());
        } else {
            mapUrlBuilder = mapUrlBuilder.queryParam("q", location);
        }
        model.addAttribute("mapUrl", mapUrlBuilder.build());

        UriBuilder streetViewUrlBuilder = UriComponentsBuilder
                .fromUriString("https://www.google.com/maps/embed/v1/")
                .path("streetview")
                .queryParam("key", gmapsApiKey)
                .queryParam("location", location)
                .queryParam("fov", 90);
        model.addAttribute("streetViewUrl", streetViewUrlBuilder.build());

        // select the "view-experience" template for rendering
        // Spring will automatically pass the model object and other contextual data to the template engine
        return "experience/view-experience";
    }

    @GetMapping(path = "/create")
    public String initCreateForm(Model model) {
        if (!authUserProvider.isAnyUser()) throw new AccessDeniedException("Access denied");

        CreateUpdateExperienceDto experienceDto = new CreateUpdateExperienceDto();
        model.addAttribute("experienceDto", experienceDto);
        return "experience/create-update-experience";
    }

    @PostMapping(path = "/create")
    public String createExperience(
            @Valid @ModelAttribute("experienceDto") CreateUpdateExperienceDto experienceDto,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("autocompleteText", generateAutocompleteText(experienceDto));
            return "experience/create-update-experience";
        } else {
            long experienceId = experienceService.createExperience(experienceDto);
            return "redirect:" + urlGenerator.view(experienceId);
        }
    }

    @GetMapping(path = "/{hashId}/update")
    public String initUpdateForm(@PathVariable String hashId, Model model) {
        long experienceId = hashIdEncoder.decode(hashId);
        Experience experience = experienceService.getExperience(experienceId);
        if (experience == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        if (!authUserProvider.isUserWithId(experience.getUserId())) throw new AccessDeniedException("Access denied");

        CreateUpdateExperienceDto experienceDto = new CreateUpdateExperienceDto(experience);
        model.addAttribute("experienceId", experienceId);
        model.addAttribute("experienceDto", experienceDto);
        model.addAttribute("autocompleteText", generateAutocompleteText(experienceDto));
        return "experience/create-update-experience";
    }

    @PostMapping(path = "/{hashId}/update")
    public String updateExperience(
            @PathVariable String hashId,
            @Valid @ModelAttribute("experienceDto") CreateUpdateExperienceDto experienceDto,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("autocompleteText", generateAutocompleteText(experienceDto));
            return "experience/create-update-experience";
        } else {
            long experienceId = hashIdEncoder.decode(hashId);
            experienceService.updateExperience(experienceId, experienceDto);
            return "redirect:" + urlGenerator.view(experienceId);
        }
    }

    @PostMapping(path = "/{hashId}/delete")
    public String deleteExperience(@PathVariable String hashId) {
        long experienceId = hashIdEncoder.decode(hashId);
        experienceService.deleteExperience(experienceId);
        return "redirect:/user/details";
    }

    @GetMapping("/{hashId}/add-to-trip")
    public String addToTripForm(@PathVariable String hashId, Model model) {
        if (!authUserProvider.isAnyUser()) return "redirect:/registration";

        long experienceId = hashIdEncoder.decode(hashId);
        User currentUser = authUserProvider.getUser();
        List<Trip> userTrips = tripService.getRecentTripsByUserId(currentUser.getUserId());
        Experience experience = experienceService.getExperience(experienceId);
        model.addAttribute("trips", userTrips);
        model.addAttribute("experience", experience);
        return "trip/add-experience-to-trip";
    }

    @PostMapping("/{hashId}/add-to-trip")
    public String addExperienceToTrip(@PathVariable String hashId, @RequestParam Long tripId) {
        if (!authUserProvider.isAnyUser()) throw new AccessDeniedException("Access denied");

        long experienceId = hashIdEncoder.decode(hashId);
        tripService.addExperienceToTrip(experienceId, tripId);
        return "redirect:/trip/" + tripId + "?success=experience-added";
    }

    String generateAutocompleteText(CreateUpdateExperienceDto experienceDto) {
        if (experienceDto.getAddress() != null) return experienceDto.getAddress();
        if (experienceDto.getLocationLat() == null || experienceDto.getLocationLng() == null) return null;
        return String.format("%.6f, %.6f", experienceDto.getLocationLat(), experienceDto.getLocationLng());
    }
}
