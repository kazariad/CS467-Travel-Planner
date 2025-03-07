package edu.oregonstate.cs467.travelplanner.user.service;

import edu.oregonstate.cs467.travelplanner.experience.service.ExperienceService;
import edu.oregonstate.cs467.travelplanner.trip.service.TripService;
import edu.oregonstate.cs467.travelplanner.user.model.User;
import edu.oregonstate.cs467.travelplanner.user.repository.UserRepository;
import edu.oregonstate.cs467.travelplanner.user.dto.UserProfileDto;
import edu.oregonstate.cs467.travelplanner.user.dto.UserRegistrationDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ExperienceService experienceService;

    @Autowired
    private TripService tripService;

    /**
     * Checks whether a username already exists in the repository.
     * @param username The username to check.
     * @return true if the username exists, false otherwise.
     */
    @Override
    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    /**
     * Saves a new user after validating that the username is unique.
     * The password is securely hashed before saving.
     * @param registrationDto The DTO containing user details upon registration.
     * @throws IllegalArgumentException if the username is already taken.
     */
    @Override
    public void save(UserRegistrationDto registrationDto) {
        if (usernameExists(registrationDto.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }
        User user = registrationDto.toEntity();
        user.setPassword(passwordEncoder.encode(user.getPassword()));   // hashed password before storing
        userRepository.save(user);
    }

    /**
     * Load user details based on the provided username.
     * This method is used by Spring Security for authentication.
     * @param username The user's username, to be retrieved.
     * @return The User object, which implements UserDetails, so it can be used in authentication.
     * @throws UsernameNotFoundException if no user is found with the provided username.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username.");
        }
        return user;
    }

    /**
     * Retrieve a user's profile details as a DTO.
     * This method extracts relevant fields from the User entity, maps them to a UserProfileDto.
     * @param user The User entity to be converted.
     * @return A UserProfileDto object containing the user's profile details
     */
    @Override
    public UserProfileDto getUserProfile(User user) {
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setFullName(user.getFullName());
        userProfileDto.setUsername(user.getUsername());
        userProfileDto.setExperienceList(experienceService.findByUserId(user.getUserId()));
        userProfileDto.setTripList(tripService.getTripsByUserId(user.getUserId()));
        return userProfileDto;
    }

    /**
     * Updates the password of a given user and records the update timestamp.
     * The new password is securely encoded before being saved to the database.
     *
     * @param user        The User entity whose password is being updated.
     * @param newPassword The new password to be set for the user.
     */
    @Transactional
    @Override
    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);
    }

    /**
     * Finds a user by their unique ID.
     * @param userId The ID associated with the user to find.
     * @return  An Optional containing the user if found, empty Optional otherwise.
     */
    @Override
    public Optional<User> findById(long userId) {
        return userRepository.findById(userId);
    }
}
