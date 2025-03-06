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
@RequestMapping("/trip")
public class TripController {

    @Autowired
    private TripService tripService;

    @Autowired
    private ExperienceService experienceService;

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
        // Ensure experienceList is initialized to avoid null in the template
        if (trip.getExperienceList() == null) {
            trip.setExperienceList(new ArrayList<>());
        }
        List<Experience> experiences = experienceService.getExperiencesByIds(trip.getExperienceList());

        // Add 'success' to the model if it exists
        if ("true".equals(success)) {
            model.addAttribute("successMessage", "Trip updated successfully!");
        }

        model.addAttribute("trip", trip);
        model.addAttribute("experiences", experiences);
        return "trip/trip-details";
    }

    /**
     * Show the trip creation form.
     */
    @GetMapping(path = "/create")
    public String showAddTripForm(Model model) {
        model.addAttribute("trip", new TripDto());
        return "trip/create-update-trip";
    }

    @GetMapping(path = "/edit/{tripId}")
    public String editTripForm(@PathVariable Long tripId, Model model) {
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
        return "trip/create-update-trip";
    }

    @PostMapping(path = "/save/{tripId}")
    public String saveTrip(@PathVariable Long tripId, @Valid @ModelAttribute("trip") TripDto tripDto,
                           BindingResult result, Model model) {
        if (!authUserProvider.isAnyUser()) throw new AccessDeniedException("Access denied");

        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "trip/create-update-trip";
        }
        try{
            if (tripDto.getTripTitle() == null || tripId == 0) {
                tripService.createTrip(tripDto);
            } else {
                tripService.updateTrip(tripId, tripDto);
            }
            return "redirect:/trip/{tripId}?success=true";
        } catch (IllegalArgumentException e) {
            // Add the error message to the model for displaying in the view
            model.addAttribute("error", e.getMessage());
            return "trip/create-update-trip";
        }
    }

    @PostMapping(path = "/delete/{tripId}")
    public String deleteTrip(@PathVariable long tripId) {
        if (!authUserProvider.isAnyUser()) throw new AccessDeniedException("Access denied");
        tripService.deleteTrip(tripId);
        return "redirect:/user/details?success=true";
    }
}