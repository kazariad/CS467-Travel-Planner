package edu.oregonstate.cs467.travelplanner.user.controller;

import edu.oregonstate.cs467.travelplanner.user.model.User;
import edu.oregonstate.cs467.travelplanner.user.service.UserService;
import edu.oregonstate.cs467.travelplanner.user.dto.UserProfileDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class UserProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/details")
    public String viewUserDetails(Model model, @AuthenticationPrincipal User user) {
        UserProfileDto userProfileDto = userService.getUserProfile(user);
        model.addAttribute("user", userProfileDto);
        return "user/user-details";
    }

    @GetMapping("/user/update-password")
    public String loadUpdatePasswordForm(Model model) {
        model.addAttribute("newPassword", ""); // Must initialize newPassword for Thymeleaf binding
        model.addAttribute("logoutWarning", "Note: You will be logged out after changing your password.");
        return "user/update-password";
    }

    @PostMapping("/user/update-password")
    public String updatePassword(@RequestParam("newPassword") String newPassword,
                                 @AuthenticationPrincipal User currentUser, HttpServletRequest
                                 request) {
        System.out.println("Received newPassword: " + newPassword);
        if (currentUser == null) {
            throw new IllegalStateException("No authenticated user found.");
        }

        // Update the password in the database
        userService.updatePassword(currentUser, newPassword);

        // log out the user
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Return a redirect to the login page on a successful update
        return "redirect:/login?passwordUpdated=true";

    }
}
