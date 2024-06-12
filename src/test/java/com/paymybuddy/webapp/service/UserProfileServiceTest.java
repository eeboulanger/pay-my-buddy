package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.UserDTO;
import com.paymybuddy.webapp.model.User;
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
    private IAuthenticationFacade facade;
    @Mock
    private Authentication authentication;
    @Mock
    private BCryptPasswordEncoder encoder;
    @Mock
    private IUserService userService;
    @InjectMocks
    private UserProfileService profileService;
    private User authUser;

    @BeforeEach
    public void setUp() {
        authUser = new User();
        authUser.setEmail("john_doe@mail.com");
        authUser.setId(1);
        authUser.setPassword("ValidPassword@12");
        authUser.setUsername("John_Doe");
    }

    @Test
    public void updateUserSuccessTest() {
        UserDTO updatedUser = new UserDTO();
        updatedUser.setEmail("john_doe@mail.com");
        updatedUser.setUsername("My new username");
        updatedUser.setPassword("NewValidPass@12");

        when(facade.getAuthentication()).thenReturn(authentication);
        when(userService.getUserByEmail(authentication.getName())).thenReturn(Optional.of(authUser));

        assertDoesNotThrow(() -> profileService.updateUser(updatedUser));
        verify(facade).getAuthentication();
        verify(userService, times(1)).getUserByEmail(authentication.getName());
    }

    @Test
    @DisplayName("Given the authenticated user cannot be found, then throw exception")
    public void updateUserFailsTest() {
        UserDTO updatedUser = new UserDTO();

        when(facade.getAuthentication()).thenReturn(authentication);
        when(userService.getUserByEmail(authentication.getName())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> profileService.updateUser(updatedUser));
        verify(facade).getAuthentication();
        verify(userService, times(1)).getUserByEmail(authentication.getName());
    }
}
