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

    @GetMapping("/user/details")
    public String viewUserDetails(@RequestParam(name = "success", required = false) String success,
                                  @RequestParam(name = "action", required = false) String action,
                                  Model model) {
        if (!authUserProvider.isAnyUser()) throw new AccessDeniedException("Access denied");
        User user = authUserProvider.getUser();
        UserProfileDto userProfileDto = userService.getUserProfile(user);
        if ("true".equals(success)) {
            if ("delete".equals(action)) {
                model.addAttribute("successMessage", "Trip was successfully deleted!");
            } else if ("add".equals(action)) {
                model.addAttribute("successMessage", "Trip was successfully added!");
            } else {
                model.addAttribute("successMessage", "Operation successful!");
            }
        }
        model.addAttribute("user", userProfileDto);
        return "user/user-details";
    }


    @GetMapping("/user/update-password")
    public String loadUpdatePasswordForm(Model model) {
        model.addAttribute("newPassword", "");
        model.addAttribute("logoutWarning", "Note: You will be logged out after changing your password.");
        return "user/update-password";
    }

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
