package com.paymybuddy.webapp.service;


import com.paymybuddy.webapp.model.Client;
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
    private ClientService service;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Test
    public void loadUserByUsername_WhenUserExists_ShouldReturnUserDetails() {
        String email = "joe@mail.com";
        Client client = new Client();
        client.setEmail(email);
        client.setPassword("password");
        when(service.getClientByEmail(email)).thenReturn(Optional.of(client));

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));

        verify(service, times(1)).getClientByEmail(email);
    }

    @Test
    public void loadUserByUsername_WhenUserDoesNotExist_ShouldThrowUsernameNotFoundException() {
        String email = "nonexistent@mail.com";
        when(service.getClientByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(email));

        verify(service, times(1)).getClientByEmail(email);
    }
}
