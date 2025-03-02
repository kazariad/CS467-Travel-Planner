package edu.oregonstate.cs467.travelplanner.user.controller;

import edu.oregonstate.cs467.travelplanner.user.model.User;
import edu.oregonstate.cs467.travelplanner.user.service.UserService;
import edu.oregonstate.cs467.travelplanner.user.dto.UserProfileDto;
import edu.oregonstate.cs467.travelplanner.util.security.AuthenticatedUserProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
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

    @Autowired
    private AuthenticatedUserProvider authUserProvider;

    /**
     * Handles the request to view the details of the currently authenticated user.
     *
     * @param model the model instance to add attributes to render the view
     * @return the name of the view template to render (e.g., "user/user-details")
     * @throws AccessDeniedException if no user is authenticated
     */
    @GetMapping("/user/details")
    public String viewUserDetails(Model model) {
        if (!authUserProvider.isAnyUser()) throw new AccessDeniedException("Access denied");
        User user = authUserProvider.getUser();
        UserProfileDto userProfileDto = userService.getUserProfile(user);
        model.addAttribute("user", userProfileDto);
        return "user/user-details";
    }

    /**
     * Handles the GET request to load the form for updating the user's password.
     * Adds necessary attributes to the model to render the form view.
     *
     * @param model the model instance to add attributes to render the view
     * @return the name of the view template used to display the password update form, "user/update-password"
     */
    @GetMapping("/user/update-password")
    public String loadUpdatePasswordForm(Model model) {
        model.addAttribute("newPassword", "");
        model.addAttribute("logoutWarning", "Note: You will be logged out after changing your password.");
        return "user/update-password";
    }

    /**
     * Handles the POST request to update the password of the currently authenticated user.
     * This method ensures that the authenticated user's password is securely updated
     * in the database and logs the user out of the session.
     *
     * @param newPassword the new password to set for the authenticated user
     * @param request the current HTTP servlet request containing session information
     * @return a redirection URL to the login page with a success indicator
     * @throws AccessDeniedException if no user is currently authenticated
     */
    @PostMapping("/user/update-password")
    public String updatePassword(@RequestParam("newPassword") String newPassword, HttpServletRequest request) {
        if (!authUserProvider.isAnyUser()) throw new AccessDeniedException("Access denied");
        User user = authUserProvider.getUser();
        userService.updatePassword(user, newPassword);
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login?success=true";
    }
}
