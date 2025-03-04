package edu.oregonstate.cs467.travelplanner.user.controller;

import edu.oregonstate.cs467.travelplanner.user.service.UserService;
import edu.oregonstate.cs467.travelplanner.user.dto.UserRegistrationDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm() {
        return "registration";
    }

    @PostMapping
    public String registerUserAccount(@Valid @ModelAttribute("user") UserRegistrationDto registrationDto,
                                      BindingResult result, Model model) {
        // check if validation already caught issues then ensures username is unique
        if (!result.hasErrors() && userService.usernameExists(registrationDto.getUsername())) {
            result.rejectValue("username", "error.user", "Username already exists");
        }
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "registration";
        }
        userService.save(registrationDto);
        return "redirect:/registration?success";
    }
}
