package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.UserDTO;
import com.paymybuddy.webapp.exception.ProfileException;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.security.IAuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserProfileServiceTest {
    @Mock
    private IAuthenticationService authenticationService;
    @Mock
    private Authentication authentication;
    @Mock
    private BCryptPasswordEncoder encoder;
    @Mock
    private IUserService userService;
    @InjectMocks
    private UserProfileService profileService;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setEmail("john_doe@mail.com");
        user.setId(1);
        user.setPassword("ValidPassword@12");
        user.setUsername("John_Doe");
    }

    @Test
    public void updateUserSuccessTest() {
        UserDTO updatedUser = new UserDTO();
        updatedUser.setEmail("john_doe@mail.com");
        updatedUser.setUsername("My new username");
        updatedUser.setPassword("NewValidPass@12");

        when(authenticationService.getCurrentUser()).thenReturn(user);

        assertDoesNotThrow(() -> profileService.updateUser(updatedUser));
        verify(authenticationService).getCurrentUser();
        verify(userService, times(1)).saveUser(user);
    }

    @Test
    @DisplayName("Given the authenticated user cannot be found, then throw exception")
    public void updateUserFailsTest() {
        UserDTO updatedUser = new UserDTO();

        when(authenticationService.getCurrentUser()).thenThrow(UsernameNotFoundException.class);

        assertThrows(UsernameNotFoundException.class, () -> profileService.updateUser(updatedUser));
        verify(authenticationService).getCurrentUser();
    }

    @Test
    @DisplayName("Given the email is already in use, when update user, then throw exception")
    public void updateUser_whenEmailExistsTest() {
        UserDTO dto = new UserDTO();
        dto.setEmail("jane_doe@mail.com");
        when(authenticationService.getCurrentUser()).thenReturn(user);
        when(userService.getUserByEmail(dto.getEmail())).thenReturn(Optional.of(user));

        assertThrows(ProfileException.class, () -> profileService.updateUser(dto));
        verify(authenticationService).getCurrentUser();
    }
}
