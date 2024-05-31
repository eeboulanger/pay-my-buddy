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
public class ConnexionControllerTest {
    @Mock
    private ISignUpService service;
    @InjectMocks
    private ConnexionController controller;

    @Test
    @DisplayName("Given there's no client with the given email, then create new client")
    public void signUpTest() throws RegistrationException {
        RegistrationForm form = new RegistrationForm();
        when(service.signUp(form)).thenReturn(true);

        String viewName = controller.createNewClient(form);

        verify(service, times(1)).signUp(form);
        assertEquals(viewName, "redirect:/index");
    }

    @Test
    @DisplayName("Given there's a client with the given email, then don't create new client")
    public void signUpFailsTest() throws RegistrationException {
        RegistrationForm form = new RegistrationForm();
        when(service.signUp(form)).thenReturn(false);

        String viewName = controller.createNewClient(form);

        verify(service, times(1)).signUp(form);
        assertEquals(viewName, "redirect:/error");
    }
}
