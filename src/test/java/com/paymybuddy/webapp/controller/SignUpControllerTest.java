package com.paymybuddy.webapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.webapp.config.SpringSecurityConfiguration;
import com.paymybuddy.webapp.dto.RegistrationForm;
import com.paymybuddy.webapp.exception.RegistrationException;
import com.paymybuddy.webapp.repository.UserRepository;
import com.paymybuddy.webapp.service.CustomUserDetailsService;
import com.paymybuddy.webapp.service.ISignUpService;
import com.paymybuddy.webapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SignUpController.class)
@Import(SpringSecurityConfiguration.class)
public class SignUpControllerTest {
    @MockBean
    private CustomUserDetailsService userDetailsService;
    @MockBean
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ISignUpService service;
    @InjectMocks
    private SignUpController controller;
    private RegistrationForm form;
    private final ObjectMapper mapper=new ObjectMapper();

    @BeforeEach
    public void setUp() {
        form = new RegistrationForm();
        form.setEmail("john_doe@mail.com");
        form.setPassword("ValidPassword@123");
        form.setUsername("john_doe");
    }

    @Test
    @DisplayName("Given there's no user with the given email, then create new user should succeed")
    public void signUpSuccessTest() throws Exception {
        mockMvc.perform(post("/users/signup")
                        .with(csrf())
                        .content(mapper.writeValueAsString(form))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index"));

        verify(service, times(1)).signUp(form);
    }

    @Test
    @DisplayName("Invalid password when sign up should return error")
    public void givenInvalidPassword_whenSignUp_thenReturnError() throws Exception {
        RegistrationForm form = new RegistrationForm();
        form.setEmail("john_doe@mail.com");
        form.setPassword("unvalid_password");
        form.setUsername("john_doe");
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/users/signup")
                        .with(csrf())
                        .content(mapper.writeValueAsString(form))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }

    @Test
    @DisplayName("Invalid email when signup should return error")
    public void givenInvalidEmail_whenSignUp_thenReturnError() throws Exception {
        RegistrationForm form = new RegistrationForm();
        form.setEmail("notvalidmail.com");
        form.setPassword("ValidPassword@123");
        form.setUsername("john_doe");
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/users/signup")
                        .with(csrf())
                        .content(mapper.writeValueAsString(form))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }

    @Test
    @DisplayName("Given user already exists when signup then return error")
    public void userAlreadyExists_shouldReturnError() throws Exception {
        RegistrationForm form = new RegistrationForm();
        form.setEmail("john_doe@gmail.com");
        form.setPassword("ValidPassword@123");
        form.setUsername("john_doe");
        ObjectMapper mapper = new ObjectMapper();

        doThrow(new RegistrationException("Failed to create user")).when(service).signUp(form);

        mockMvc.perform(post("/users/signup")
                        .with(csrf())
                        .content(mapper.writeValueAsString(form))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));

        verify(service, times(1)).signUp(form);
    }
}
