package edu.oregonstate.cs467.travelplanner.user.service;

import edu.oregonstate.cs467.travelplanner.user.model.User;
import edu.oregonstate.cs467.travelplanner.user.repository.UserRepository;
import edu.oregonstate.cs467.travelplanner.web.dto.UserRegistrationDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    /**
     * Test that the usernameExists() method returns true when the username exists in the repository.
     */
    @Test
    public void testUsernameExistsTrue() {
        User testUser = new User();
        testUser.setUserId(1L);
        testUser.setFullName("Test User");
        testUser.setUsername("testUser");
        testUser.setPassword("encodedPassword");

        when(userRepository.findByUsername("testUser")).thenReturn(testUser);
        assertTrue(userService.usernameExists("testUser"));
        verify(userRepository).findByUsername(("testUser"));
    }

    /**
     * Test that the usernameExists() method returns true when the username exists in the repository.
     */
    @Test
    public void testUsernameExistsFalse() {
        User testUser = new User();
        testUser.setUserId(1L);
        testUser.setFullName("Test User");
        testUser.setUsername("testUser");
        testUser.setPassword("encodedPassword");

        when(userRepository.findByUsername("unknownUser")).thenReturn(null);
        assertFalse(userService.usernameExists("unknownUser"));
        verify(userRepository).findByUsername(("unknownUser"));
    }

    /**
     * Test that a new user is successfully saved when the username does not already exist.
     */
    @Test
    public void testSaveNewUserSuccess() {
        UserRegistrationDto testUserRegistrationDto = new UserRegistrationDto();
        testUserRegistrationDto.setUsername("newUser");
        testUserRegistrationDto.setPassword("plainPassword");

        // mock repository behavior and password encoding
        when(userRepository.findByUsername("newUser")).thenReturn(null);
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");

        // capture the actual User object from save()
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        userService.save(testUserRegistrationDto);

        // ensure the save() was called and captured its argument
        verify(userRepository, times(1)).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        User expectedUser = testUserRegistrationDto.toEntity();
        expectedUser.setPassword("encodedPassword");
        assertEquals(expectedUser.getUsername(), capturedUser.getUsername());
        assertEquals(expectedUser.getPassword(), capturedUser.getPassword());
    }

    /**
     * Test that saving a user with an existing username throws an exception.
     */
    @Test
    public void testSaveExistingUserThrowException() {
        User testUser = new User();
        testUser.setUserId(1L);
        testUser.setFullName("Test User");
        testUser.setUsername("testUser");
        testUser.setPassword("encodedPassword");

        UserRegistrationDto testUserRegistrationDto = new UserRegistrationDto();
        testUserRegistrationDto.setUsername("testUser");

        // mock repository behavior : simulate username is already taken
        when(userRepository.findByUsername("testUser")).thenReturn(testUser);

        // ensure an exception is thrown when it tries to save a duplicate user
        Exception exception = null;
        try {
            userService.save(testUserRegistrationDto);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals("Username already taken", exception.getMessage());

        // ensure the user is NOT saved to the repository
        verify(userRepository, never()).save(any());
    }

    /**
     * Tests that loadUserByUsername() successfully retrieves a user.
     */
    @Test
    public void testLoadUserByUsernameSuccess() {
        User testUser = new User();
        testUser.setUserId(1L);
        testUser.setFullName("Test User");
        testUser.setUsername("testUser");
        testUser.setPassword("encodedPassword");

        // mock repository behavior
        when(userRepository.findByUsername("testUser")).thenReturn(testUser);

        // create an ArgumentCaptor to capture the actual User object from findByUsername()
        ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass(String.class);

        // cal the method being tested
        UserDetails userDetails = userService.loadUserByUsername("testUser");

        verify(userRepository, times(1)).findByUsername(usernameCaptor.capture());
        assertNotNull(userDetails);
        assertEquals("testUser", usernameCaptor.getValue());
        assertEquals("testUser", userDetails.getUsername());
    }

    /**
     * Tests that loadUserByUsername() throws UsernameNotFoundException for an unknown username.
     */
    @Test
    public void testLoadUserByUsernameThrowsException() {
        // mock repository behavior
        when(userRepository.findByUsername("unknownUser")).thenReturn(null);

        // ensure an exception is thrown
        Exception exception = null;
        try {
            userService.loadUserByUsername("unknownUser");
        } catch (UsernameNotFoundException e) {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals("Invalid username.", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("unknownUser");
    }
}
