package edu.oregonstate.cs467.travelplanner.user.service;

import edu.oregonstate.cs467.travelplanner.user.model.User;
import edu.oregonstate.cs467.travelplanner.user.repository.UserRepository;
import edu.oregonstate.cs467.travelplanner.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public User save(UserRegistrationDto registrationDto) {
        if (usernameExists(registrationDto.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }
        User user = registrationDto.toEntity();
        user.setPassword(passwordEncoder.encode(user.getPassword()));   // hashed password before storing
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username.");
        }
        return user;
    }

}
