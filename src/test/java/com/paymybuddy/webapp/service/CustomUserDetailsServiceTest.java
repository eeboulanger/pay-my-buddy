package com.paymybuddy.webapp.service;


import com.paymybuddy.webapp.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserService service;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Test
    public void loadUserByUsername_WhenUserExists_ShouldReturnUserDetails() {
        String email = "joe@mail.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("password");
        when(service.getUserByEmail(email)).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));

        verify(service, times(1)).getUserByEmail(email);
    }

    @Test
    public void loadUserByUsername_WhenUserDoesNotExist_ShouldThrowUsernameNotFoundException() {
        String email = "nonexistent@mail.com";
        when(service.getUserByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(email));

        verify(service, times(1)).getUserByEmail(email);
    }
}
