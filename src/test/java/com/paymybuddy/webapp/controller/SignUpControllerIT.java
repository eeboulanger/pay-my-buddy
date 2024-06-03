package com.paymybuddy.webapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.webapp.dto.RegistrationForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
public class SignUpControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Given there's no client with the given email address, then create new client")
    public void signUpTest() throws Exception {
        //New user
        RegistrationForm form = new RegistrationForm("maria_doe@mail.com", "123@Abcd", "Maria");
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/public/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(form)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index"));
    }

    @Test
    @DisplayName("Given there's a client with the given email address, then don't create new client")
    public void signUpFailsTest() throws Exception {
        //New user
        RegistrationForm form = new RegistrationForm("john_doe@mail.com", "password", "John");
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/public/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(form)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }

    @Test
    @DisplayName("Given the email address isn't valid, then show error")
    public void givenEmailIsNotValid_whenCreateNewClient_thenShowError() throws Exception {
        //New user
        RegistrationForm form = new RegistrationForm("maria_doemail.com", "123@Abcd", "Maria");
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/public/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(form)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }

    @Test
    @DisplayName("Given the password isn't valid, then show error")
    public void givenPasswordIsNotValid_whenCreateNewClient_thenShowError() throws Exception {
        //New user
        RegistrationForm form = new RegistrationForm("maria_doe@mail.com", "AAAAAbcd", "Maria");
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/public/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(form)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }

    @Test
    public void userLoginTest() throws Exception {
        mockMvc.perform(formLogin("/login").user("john_doe@mail.com")
                .password("password")).andExpect(authenticated());
    }

    @Test
    public void userLoginFailed() throws Exception {
        mockMvc.perform(formLogin("/login").user("user").password("wrongpassword"))
                .andExpect(unauthenticated());
    }
}
