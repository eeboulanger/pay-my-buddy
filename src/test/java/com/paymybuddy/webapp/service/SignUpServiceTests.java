package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.RegistrationForm;
import com.paymybuddy.webapp.exception.EmailNotUniqueException;
import com.paymybuddy.webapp.exception.EmailNotValidException;
import com.paymybuddy.webapp.exception.PasswordNotValidException;
import com.paymybuddy.webapp.exception.RegistrationException;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.utils.IValidationUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SignUpServiceTests {
    @Mock
    private ClientService clientService;
    @Mock
    private IValidationUtil validationUtil;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @InjectMocks
    private SignUpService service;

    @Test
    @DisplayName("Given there is no client with the given email address, when sign up new client, then return true")
    public void givenEmailDoesNotExist_whenSignUp_thenCreateNewClient() throws RegistrationException {
        RegistrationForm client = new RegistrationForm();
        client.setEmail("john_doe@mail.com");
        client.setPassword("password");
        when(clientService.getClientByEmail(client.getEmail())).thenReturn(Optional.empty());
        when(validationUtil.validateEmail("john_doe@mail.com")).thenReturn(true);
        when(validationUtil.validatePassword("password")).thenReturn(true);

        boolean result = service.signUp(client);

        assertTrue(result);
    }

    @Test
    @DisplayName("Given there is a client with the given email address, when sign up new client, then throw email not unique exception")
    public void givenEmailExistsInDb_whenSignUp_thenDoNotCreateNewClient() {
        RegistrationForm form = new RegistrationForm();
        form.setEmail("john_doe@mail.com");
        form.setPassword("password");
        User user = new User();
        when(validationUtil.validateEmail("john_doe@mail.com")).thenReturn(true);
        when(validationUtil.validatePassword("password")).thenReturn(true);
        when(clientService.getClientByEmail(form.getEmail())).thenReturn(Optional.of(user));

        assertThrows(EmailNotUniqueException.class, () -> service.signUp(form));
    }

    @Test
    @DisplayName("Given the email is not valid, when sign up new client, then throw exception")
    public void givenEmailNotValid_whenSignUp_thenThrowException() {
        RegistrationForm form = new RegistrationForm();
        form.setEmail("john_doe@mail.com");
        form.setPassword("password");
        when(validationUtil.validateEmail("john_doe@mail.com")).thenReturn(false);

        assertThrows(EmailNotValidException.class, () -> service.signUp(form));
    }

    @Test
    @DisplayName("Given the password is not valid, when sign up new client, then throw exception")
    public void givenPasswordIsNotValid_whenSignUp_thenDoNotCreateNewClient() {
        RegistrationForm form = new RegistrationForm();
        form.setEmail("john_doe@mail.com");
        form.setPassword("password");
        when(validationUtil.validateEmail("john_doe@mail.com")).thenReturn(true);
        when(validationUtil.validatePassword("password")).thenReturn(false);

        assertThrows(PasswordNotValidException.class, () -> service.signUp(form));
    }
}
