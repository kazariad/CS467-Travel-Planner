package edu.oregonstate.cs467.travelplanner.user.service;

import edu.oregonstate.cs467.travelplanner.user.model.User;
import edu.oregonstate.cs467.travelplanner.user.dto.UserProfileDto;
import edu.oregonstate.cs467.travelplanner.user.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    void save(UserRegistrationDto registrationDto);
    boolean usernameExists(String username);

    UserProfileDto getUserProfile(User user);

    Optional<User> findById(long userId);
}
