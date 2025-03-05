package edu.oregonstate.cs467.travelplanner.trip.controller;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import edu.oregonstate.cs467.travelplanner.experience.service.ExperienceService;
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
public class TripController {

    @Autowired
    private TripService tripService;

    @Autowired
    private ExperienceService experienceService;

    @Autowired
    private AuthenticatedUserProvider authUserProvider;

    @GetMapping("/trip-details/{tripId}")
    public String viewTripDetails(@PathVariable("tripId") Long tripId, Model model) {
        if (!authUserProvider.isAnyUser()) {
            throw new AccessDeniedException("Access denied");
        }
        User user = authUserProvider.getUser();
        Trip trip = tripService.getTripById(tripId);
        if (trip == null) {
            throw new IllegalArgumentException("Trip not found for id: " + tripId);
        }
        List<Experience> experiences = experienceService.findByUserId(user.getUserId());

        // Ensure experienceList is initialized to avoid null in the template
        if (user.getExperienceList() == null) {
            user.setExperienceList(new ArrayList<>());
        }

        model.addAttribute("trip", trip);
        model.addAttribute("experiences", experiences);
        return "trip/trip-details";
    }

    /**
     * Show the trip creation form.
     */
    @GetMapping("/add-trip")
    public String showAddTripForm(Model model) {
        model.addAttribute("trip", new TripDto());
        return "trip/create-update-trip";
    }

    /**
     * Handle form submission to persist the trip.
     */
    @PostMapping("/add-trip")
    public String addTrip(@Valid @ModelAttribute("trip") TripDto tripDto,
                          BindingResult result, Model model) {
        if (!authUserProvider.isAnyUser()) throw new AccessDeniedException("Access denied");
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "trip/create-update-trip";
        }
        tripService.addTrip(tripDto);
        return "redirect:/user/details?success=true";
    }
}