package edu.oregonstate.cs467.travelplanner.user.controller;

import edu.oregonstate.cs467.travelplanner.user.model.User;
import edu.oregonstate.cs467.travelplanner.user.service.UserService;
import edu.oregonstate.cs467.travelplanner.web.dto.UserProfileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class UserProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/details")
    public String viewUserDetails(Model model, @AuthenticationPrincipal User user) {
        UserProfileDto userProfileDto = userService.getUserProfile(user);
        model.addAttribute("user", userProfileDto);
        return "user-details";
    }
}
