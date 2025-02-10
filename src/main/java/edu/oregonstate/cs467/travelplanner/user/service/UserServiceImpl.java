package edu.oregonstate.cs467.travelplanner.user.service;

import edu.oregonstate.cs467.travelplanner.user.model.User;
import edu.oregonstate.cs467.travelplanner.user.repository.UserRepository;
import edu.oregonstate.cs467.travelplanner.web.dto.UserProfileDto;
import edu.oregonstate.cs467.travelplanner.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    @Override
    public void save(UserRegistrationDto registrationDto) {
        if (usernameExists(registrationDto.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }
        User user = registrationDto.toEntity();
        user.setPassword(passwordEncoder.encode(user.getPassword()));   // hashed password before storing
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username.");
        }
        return user;
    }

    /**
     * Maps the relevant fields from the User object to the UserProfileDto, allowing the user details to be displayed
     * in the view layer
     * @param user The User entity to be converted
     * @return A UserProfileDto object containing the user's profile details
     */
    @Override
    public UserProfileDto getUserProfile(User user) {
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setFullName(user.getFullName());
        userProfileDto.setUsername(user.getUsername());
        userProfileDto.setExperienceList(user.getExperienceList());
        return userProfileDto;
    }

    @Override
    public Optional<User> findById(long userId) {
        return userRepository.findById(userId);
    }
}
