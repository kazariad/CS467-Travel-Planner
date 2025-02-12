package edu.oregonstate.cs467.travelplanner.user.service;

import edu.oregonstate.cs467.travelplanner.user.model.User;
import edu.oregonstate.cs467.travelplanner.user.repository.UserRepository;
import edu.oregonstate.cs467.travelplanner.web.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

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

    @Test
    public void testSaveNewUserSuccess() {
        UserRegistrationDto testUserRegistrationDto = new UserRegistrationDto();
        testUserRegistrationDto.setUsername("newUser");
        testUserRegistrationDto.setPassword("plainPassword");

        // mocks repository behavior and password encoding
        when(userRepository.findByUsername("newUser")).thenReturn(null);
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");

        // captures the actual User object from save()
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        userService.save(testUserRegistrationDto);
        verify(userRepository, times(1)).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        User expectedUser = testUserRegistrationDto.toEntity();
        expectedUser.setPassword("encodedPassword");
        assertEquals(expectedUser.getUsername(), capturedUser.getUsername());
        assertEquals(expectedUser.getPassword(), capturedUser.getPassword());
    }
}
