package com.paymybuddy.webapp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
public class LogInControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void userLoginTest() throws Exception {
        mockMvc.perform(formLogin("/login").user("john_doe@mail.com")
                .password("1234@Abcd")).andExpect(authenticated());
    }

    @Test
    public void userLoginFailed() throws Exception {
        mockMvc.perform(formLogin("/login").user("user").password("wrongpassword"))
                .andExpect(unauthenticated());
    }
}
