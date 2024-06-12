package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.config.SpringSecurityConfiguration;
import com.paymybuddy.webapp.dto.UserDTO;
import com.paymybuddy.webapp.exception.RegistrationException;
import com.paymybuddy.webapp.repository.UserRepository;
import com.paymybuddy.webapp.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SignUpController.class)
@Import(SpringSecurityConfiguration.class)
public class SignUpControllerTest {
    @MockBean
    private CustomUserDetailsService userDetailsService;
    @MockBean
    private IUserService userService;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ISignUpService service;
    @InjectMocks
    private UserController controller;
    private UserDTO userDTO;

    @BeforeEach
    public void setUp() {
        userDTO = new UserDTO();
        userDTO.setEmail("john_doe@mail.com");
        userDTO.setPassword("ValidPassword@123");
        userDTO.setUsername("john_doe");
    }

    @Test
    @DisplayName("Given there's no user with the given email, then create new user should succeed")
    public void signUpSuccessTest() throws Exception {
        mockMvc.perform(post("/signup")
                        .with(csrf())
                        .param("email", userDTO.getEmail())
                        .param("password", userDTO.getPassword())
                        .param("username", userDTO.getUsername()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(service, times(1)).signUp(userDTO);
    }

    @Test
    @DisplayName("Invalid password when sign up should return error")
    public void givenInvalidPassword_whenSignUp_thenReturnError() throws Exception {
        userDTO.setPassword("unvalid_password");

        mockMvc.perform(post("/signup")
                        .with(csrf())
                        .param("username", userDTO.getUsername())
                        .param("email", userDTO.getEmail())
                        .param("password", userDTO.getPassword()))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().attributeHasFieldErrors("userDTO", "password"));

        verify(service, never()).signUp(userDTO);
    }

    @Test
    @DisplayName("Invalid email when signup should return error")
    public void givenInvalidEmail_whenSignUp_thenReturnError() throws Exception {
        userDTO.setEmail("notvalidmail.com");

        mockMvc.perform(post("/signup")
                        .with(csrf())
                        .param("username", userDTO.getUsername())
                        .param("email", userDTO.getEmail())
                        .param("password", userDTO.getPassword()))
                .andExpect(view().name("signup"))
                .andExpect(model().attributeHasFieldErrors("userDTO", "email"));

        verify(service, never()).signUp(userDTO);
    }

    @Test
    @DisplayName("Invalid username when signup should return error")
    public void givenInvalidUserName_whenSignUp_thenReturnError() throws Exception {
        userDTO.setUsername("");

        mockMvc.perform(post("/signup")
                        .with(csrf())
                        .param("username", userDTO.getUsername())
                        .param("email", userDTO.getEmail())
                        .param("password", userDTO.getPassword()))
                .andExpect(view().name("signup"))
                .andExpect(model().attributeHasFieldErrors("userDTO", "username"));

        verify(service, never()).signUp(userDTO);
    }

    @Test
    @DisplayName("Given user already exists when signup then return error")
    public void userAlreadyExists_shouldReturnError() throws Exception {
        doThrow(new RegistrationException("Failed to create user")).when(service).signUp(userDTO);

        mockMvc.perform(post("/signup")
                        .with(csrf())
                        .param("username", userDTO.getUsername())
                        .param("email", userDTO.getEmail())
                        .param("password", userDTO.getPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/signup"))
                .andExpect(flash().attribute("error", "Failed to create user"));

        verify(service, times(1)).signUp(userDTO);
    }
}
