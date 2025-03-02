package edu.oregonstate.cs467.travelplanner.experience.web;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import edu.oregonstate.cs467.travelplanner.experience.service.ExperienceService;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.CreateUpdateExperienceDto;
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
    private final UserService userService;
    private final TimeUtils timeUtils;
    private final String gmapsApiKey;
    private final TripService tripService;

    public ExperienceWebController(
            AuthenticatedUserProvider authUserProvider,
            ExperienceService experienceService,
            UserService userService,
            TimeUtils timeUtils,
            TripService tripService,
            @Value("${google.maps.api.key}")
            String gmapsApiKey
    ) {
        this.authUserProvider = authUserProvider;
        this.experienceService = experienceService;
        this.userService = userService;
        this.timeUtils = timeUtils;
        this.gmapsApiKey = gmapsApiKey;
        this.tripService = tripService;
    }

    @ModelAttribute
    public void initModel(Model model) {
        model.addAttribute("gmapsApiKey", gmapsApiKey);
    }

    @GetMapping("/{experienceId}")
    public String viewExperience(@PathVariable long experienceId, Model model) {
        // retrieve the Experience object from persistent storage based on the path ID
        Experience experience = experienceService.getExperience(experienceId);
        // if the Experience doesn't exist (or was deleted), return a 404 error
        if (experience == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        // add the Experience object to the model to make it accessible by the HTML template engine
        model.addAttribute("experience", experience);

        // find the User who created this Experience and add their username to the model
        User author = userService.findById(experience.getUserId()).get();
        model.addAttribute("author", author.getUsername());
        model.addAttribute("isAuthor", authUserProvider.isUserWithId(experience.getUserId()));
        // how long ago this Experience was submitted as a relative duration, e.g. "12 hours ago", "5 days ago", etc.
        // use relative time to avoid having to determine the client's timezone (surprisingly there isn't a standard header for this, requires JS)
        model.addAttribute("submittedDuration", timeUtils.formatDuration(Duration.between(experience.getCreatedAt(), Instant.now())));

        if (experience.getRatingCnt() > 0) {
            // the Experience avg. rating is calculated by dividing the cumulative rating sum by the number of ratings
            // make sure Experience has at least 1 rating to avoid dividing by 0
            model.addAttribute("rating", String.format("%.1f / 5.0", (double) experience.getRatingSum() / (double) experience.getRatingCnt()));
        }

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
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "experience/create-update-experience";
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
        return "experience/create-update-experience";
    }

    @PostMapping(path = "/{experienceId}/update")
    public String updateExperience(
            @PathVariable long experienceId,
            @Valid @ModelAttribute("experienceDto") CreateUpdateExperienceDto experienceDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "experience/create-update-experience";
        } else {
            experienceService.updateExperience(experienceId, experienceDto);
            return "redirect:/experience/{experienceId}";
        }
    }

    @PostMapping(path = "/{experienceId}/delete")
    public String deleteExperience(@PathVariable long experienceId) {
        experienceService.deleteExperience(experienceId);
        return "redirect:/";
    }

    @GetMapping("/{experienceId}/add-to-trip")
    public String addToTripForm(
            @PathVariable Long experienceId,
            Model model
    ) {
        if (!authUserProvider.isAnyUser()) {
            return "redirect:/registration";
        }
        User currentUser = authUserProvider.getUser();
        List<Trip> userTrips = tripService.getRecentTripsByUserId(currentUser.getUserId());
        Experience experience = experienceService.getExperience(experienceId);
        model.addAttribute("trips", userTrips);
        model.addAttribute("experience", experience);
        return "trip/add-experience-to-trip";
    }

    @PostMapping("/{experienceId}/add-to-trip")
    public String addExperienceToTrip(
            @PathVariable Long experienceId,
            @RequestParam Long tripId
    ) {
        if (!authUserProvider.isAnyUser()) throw new AccessDeniedException("Access denied");
        tripService.addExperienceToTrip(experienceId, tripId);
        return "redirect:/trip/" + tripId + "?success=experience-added";
    }

    @GetMapping(path = "/search")
    public String searchExperiences(@ModelAttribute ExperienceSearchForm searchForm) {
        searchForm.normalize();
        ExperienceSearchParams params = convertSearchFormToParams(searchForm);
        ExperienceSearchResult result = experienceService.search(params);
        return "n/a";
    }

    ExperienceSearchParams convertSearchFormToParams(ExperienceSearchForm form) {
        ExperienceSearchParams params = new ExperienceSearchParams();
        params.setKeywords(form.getKeywords());

        if (form.getLocationLat() != null && form.getLocationLng() != null && form.getDistanceMiles() != null) {
            params.setLocation(new ExperienceSearchLocationParams(
                    form.getLocationLat(), form.getLocationLng(), form.getDistanceMiles() * 1609.34));
        }

        switch (form.getSort()) {
            case "bestmatch":
                params.setSort(ExperienceSearchSort.KEYWORD_MATCH);
                break;
            case "distance":
                params.setSort(ExperienceSearchSort.DISTANCE);
                break;
            case "rating":
                params.setSort(ExperienceSearchSort.RATING);
                break;
            case "newest":
                params.setSort(ExperienceSearchSort.NEWEST);
                break;
        }

        params.setOffset(form.getOffset());
        params.setLimit(10);
        return params;
    }
}
