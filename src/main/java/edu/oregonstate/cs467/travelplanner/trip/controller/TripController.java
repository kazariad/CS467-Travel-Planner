package edu.oregonstate.cs467.travelplanner.trip.controller;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import edu.oregonstate.cs467.travelplanner.experience.service.ExperienceService;
import edu.oregonstate.cs467.travelplanner.experience.web.util.ExperienceHashIdEncoder;
import edu.oregonstate.cs467.travelplanner.trip.model.Trip;
import edu.oregonstate.cs467.travelplanner.trip.service.TripService;
import edu.oregonstate.cs467.travelplanner.user.model.User;
import edu.oregonstate.cs467.travelplanner.util.security.AuthenticatedUserProvider;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import edu.oregonstate.cs467.travelplanner.trip.dto.TripDto;

import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("/trip")
public class TripController {

    @Autowired
    private TripService tripService;

    @Autowired
    private ExperienceService experienceService;

    @Autowired
    private ExperienceHashIdEncoder hashIdEncoder;

    @Autowired
    private AuthenticatedUserProvider authUserProvider;

    @GetMapping("/{tripId}")
    public String viewTripDetails(@PathVariable("tripId") Long tripId,
                                  @RequestParam(name = "success", required = false) String success,
                                  Model model) {
        if (!authUserProvider.isAnyUser()) {
            throw new AccessDeniedException("Access denied");
        }
        User user = authUserProvider.getUser();
        Trip trip = tripService.getTripById(tripId);
        if (trip == null) {
            throw new IllegalArgumentException("Trip not found for id: " + tripId);
        }
        if (trip.getExperienceList() == null) {
            trip.setExperienceList(new ArrayList<>());
        }
        List<Experience> experiences = experienceService.getExperiencesByIds(trip.getExperienceList());

        if ("true".equals(success)) {
            model.addAttribute("successMessage", "Trip updated successfully!");
        }
        model.addAttribute("trip", trip);
        model.addAttribute("experiences", experiences);
        return "trip/trip-details";
    }

    @GetMapping(path = "/create")
    public String showAddTripForm(Model model) {
        model.addAttribute("trip", new TripDto());
        return "trip/create-trip";
    }

    @GetMapping(path = "/update/{tripId}")
    public String showUpdateTripForm(@PathVariable Long tripId, Model model) {
        if (!authUserProvider.isAnyUser()) throw new AccessDeniedException("Access denied");

        Trip trip = tripService.getTripById(tripId);
        if (trip == null) {
            throw new IllegalArgumentException("Trip ID not found: " + tripId);
        }
        TripDto tripDto = new TripDto(
                trip.getTripTitle(),
                trip.getStartDate(),
                trip.getEndDate(),
                trip.getExperienceList()
        );
        model.addAttribute("tripId", tripId);
        model.addAttribute("trip", tripDto);
        return "trip/update-trip";
    }

    @PostMapping("/create")
    public String createTrip(@Valid @ModelAttribute("trip") TripDto tripDto, BindingResult result, Model model) {
        if (!authUserProvider.isAnyUser()) {
            throw new AccessDeniedException("Access denied");
        }

        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "trip/create-trip"; // Return to form with error messages
        }

        try {
            tripService.createTrip(tripDto);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "trip/create-trip"; // Return to form with error message
        }

        return "redirect:/user/details?success=true&action=add";
    }

    @PostMapping("/update/{tripId}")
    public String updateTrip(@PathVariable Long tripId, @Valid @ModelAttribute("trip") TripDto tripDto,
                           BindingResult result, Model model) {
        if (!authUserProvider.isAnyUser()) throw new AccessDeniedException("Access denied");
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "trip/update-trip";
        }
        try{
            tripService.updateTrip(tripId, tripDto);
            return "redirect:/trip/{tripId}?success=true";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "trip/update-trip";
        }
    }

    @PostMapping(path = "/delete/{tripId}")
    public String deleteTrip(@PathVariable long tripId) {
        if (!authUserProvider.isAnyUser()) throw new AccessDeniedException("Access denied");
        tripService.deleteTrip(tripId);
        return "redirect:/user/details?success=true&action=delete";
    }

    @PostMapping(path = "/{tripId}/experience/delete/{hashId}")
    public String deleteExperienceFromTrip(@PathVariable long tripId, @PathVariable String hashId) {
        if (!authUserProvider.isAnyUser()) throw new AccessDeniedException("Access denied");
        long experienceId = hashIdEncoder.decode(hashId);
        tripService.deleteExperienceFromTrip(tripId, experienceId);
        return "redirect:/trip/{tripId}?success=true";
    }
}