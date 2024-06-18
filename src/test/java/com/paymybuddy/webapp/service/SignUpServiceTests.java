package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.UserDTO;
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
public class SignUpServiceTests {
    @Mock
    private IUserService userService;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @InjectMocks
    private SignUpService service;

    @Test
    @DisplayName("Given there is no user with the given email address, when sign up new user, then create new user")
    public void givenEmailDoesNotExist_whenSignUp_thenCreateNewClient() throws RegistrationException {
        User user=new User();
        UserDTO dto = new UserDTO();
        dto.setEmail("john_doe@mail.com");
        dto.setPassword("password");
        when(userService.getUserByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(userService.saveUser(any(User.class))).thenReturn(user);

        service.signUp(dto);

        verify(userService, times(1)).getUserByEmail(dto.getEmail());
        verify(userService, times(2)).saveUser(any(User.class));
    }

    @Test
    @DisplayName("Given there is a user with the given email address, when sign up new user, then throw email not unique exception")
    public void givenEmailExistsInDb_whenSignUp_thenDoNotCreateNewClient() {
        UserDTO form = new UserDTO();
        form.setEmail("john_doe@mail.com");
        form.setPassword("password");
        User user = new User();
        when(userService.getUserByEmail(form.getEmail())).thenReturn(Optional.of(user));

        assertThrows(RegistrationException.class, () -> service.signUp(form));

        verify(userService, times(1)).getUserByEmail(form.getEmail());
        verify(userService, never()).saveUser(any(User.class));
    }
}
