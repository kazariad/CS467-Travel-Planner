package edu.oregonstate.cs467.travelplanner.web.dto;

import edu.oregonstate.cs467.travelplanner.user.model.User;
import edu.oregonstate.cs467.travelplanner.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class UserDetailsController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/details")
    public String viewUserDetails(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
        return "userDetails";
    }

}
