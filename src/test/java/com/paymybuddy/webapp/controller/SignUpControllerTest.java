package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.dto.RegistrationForm;
import com.paymybuddy.webapp.exception.RegistrationException;
import com.paymybuddy.webapp.service.ISignUpService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SignUpControllerTest {
    @Mock
    private ISignUpService service;
    @InjectMocks
    private SignUpController controller;

    @Test
    @DisplayName("Given there's no user with the given email, then create new client")
    public void signUpTest() throws RegistrationException {
        RegistrationForm form = new RegistrationForm();

        String viewName = controller.createNewUser(form);

        verify(service, times(1)).signUp(form);
        assertEquals( "redirect:/index", viewName);
    }

    @Test
    @DisplayName("Given there's a user with the given email, then don't create new client")
    public void signUpFailsTest() throws RegistrationException {
        RegistrationForm form = new RegistrationForm();
        doThrow(new RegistrationException("Failed to create user")).when(service).signUp(form);

        String viewName = controller.createNewUser(form);

        verify(service, times(1)).signUp(form);
        assertEquals( "redirect:/error", viewName);
    }
}
