package edu.oregonstate.cs467.travelplanner.trip.controller;

import edu.oregonstate.cs467.travelplanner.trip.service.TripService;
import edu.oregonstate.cs467.travelplanner.util.security.AuthenticatedUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import edu.oregonstate.cs467.travelplanner.trip.dto.TripDto;


@Controller
public class TripController {

    @Autowired
    private TripService tripService;

    @Autowired
    private AuthenticatedUserProvider authUserProvider;

    @GetMapping("/trip/add")
    public String showAddTripForm(Model model) {
        if (!authUserProvider.isAnyUser()) throw new AccessDeniedException("Access denied");
        model.addAttribute("trip", new TripDto());
        return "edit-trip-details";
    }

    @PostMapping("/trip/add")
    public String addTrip(@ModelAttribute("trip") TripDto tripDto) {
        if (!authUserProvider.isAnyUser()) throw new AccessDeniedException("Access denied");
        tripService.addTrip(tripDto);
        return "redirect:/user/user-details";
    }
}