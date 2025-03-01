package edu.oregonstate.cs467.travelplanner.user.service;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import edu.oregonstate.cs467.travelplanner.experience.service.ExperienceService;
import edu.oregonstate.cs467.travelplanner.user.model.User;
import edu.oregonstate.cs467.travelplanner.user.repository.UserRepository;
import edu.oregonstate.cs467.travelplanner.user.dto.UserProfileDto;
import edu.oregonstate.cs467.travelplanner.user.dto.UserRegistrationDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ExperienceService experienceService;

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

    /**
     * Tests getUserProfile() method to ensure it correctly converts a User entity into UserProfileDto
     *
     * Verifies:
     *  - The returned UserProfileDto is not null.
     *  - fullName and username fields are correctly mapped.
     *  - The
     */
    @Test
    public void testGetUserProfile() {
        User testUser = new User();
        testUser.setUserId(1L);
        testUser.setFullName("Test User");
        testUser.setUsername("testUser");

        Experience experience1 = new Experience();
        experience1.setExperienceId(2L);
        experience1.setTitle("Breathtaking Views and Vibrant Atmosphere");
        experience1.setDescription("Venice Beach oceanfront is a must-visit! The views of the Pacific are stunning, " +
                "especially at sunset. The boardwalk is lively with street performers, unique shops, and plenty " +
                "of food options. The beach itself is clean, great for a relaxing stroll or a game of volleyball. " +
                "While it can get crowded, the energy is part of the charm. Perfect for people-watching, biking, or " +
                "just soaking in the California vibes. Highly recommended for anyone looking to experience the heart " +
                "of LA’s beach culture!'");
        experience1.setEventDate(LocalDate.of(2024, 12, 18));
        experience1.setLocationLat(33.986267981122);
        experience1.setLocationLng(-118.473022732421);
        experience1.setAddress("1701 Ocean Front Walk, Venice, CA 90291");
        experience1.setImageUrl("'https://drupal-prod.visitcalifornia.com/sites/default/files/styles/fixed_300" +
                "/public/VC_California101_VeniceBeach_Stock_RF_638340372_1280x640.jpg.webp?itok=vHd_tD-I");
        experience1.setRatingCnt(1);
        experience1.setRatingSum(5);
        experience1.setUserId(1L);
        experience1.setCreatedAt(Instant.parse("2024-12-18T21:51:05.000000Z"));
        when(experienceService.findByUserId(1L)).thenReturn(List.of(experience1));

        UserProfileDto actual = userService.getUserProfile(testUser);
        UserProfileDto expected = new UserProfileDto("Test User", "testUser", List.of(experience1));
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
