package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.RegistrationForm;
import com.paymybuddy.webapp.exception.RegistrationException;
import com.paymybuddy.webapp.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserProfileServiceTests {
    @Mock
    private UserService userService;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @InjectMocks
    private UserProfileService service;

    @Test
    @DisplayName("Given there is no client with the given email address, when sign up new client, then return true")
    public void givenEmailDoesNotExist_whenSignUp_thenCreateNewClient() throws RegistrationException {
        RegistrationForm client = new RegistrationForm();
        client.setEmail("john_doe@mail.com");
        client.setPassword("password");
        when(userService.getUserByEmail(client.getEmail())).thenReturn(Optional.empty());

        service.signUp(client);

        verify(userService, times(1)).getUserByEmail(client.getEmail());
    }

    @Test
    @DisplayName("Given there is a client with the given email address, when sign up new client, then throw email not unique exception")
    public void givenEmailExistsInDb_whenSignUp_thenDoNotCreateNewClient() {
        RegistrationForm form = new RegistrationForm();
        form.setEmail("john_doe@mail.com");
        form.setPassword("password");
        User user = new User();
        when(userService.getUserByEmail(form.getEmail())).thenReturn(Optional.of(user));

        assertThrows(RegistrationException.class, () -> service.signUp(form));

        verify(userService, times(1)).getUserByEmail(form.getEmail());
    }
}
