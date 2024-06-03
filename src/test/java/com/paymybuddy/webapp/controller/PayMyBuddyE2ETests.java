package com.paymybuddy.webapp.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
public class PayMyBuddyE2ETests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Given there is a user with email, when add new user connection, then add user connection to authenticated user")
    @WithMockUser(username="john_doe@mail.com", roles= "USER")
    public void addUserConnectionTest() throws Exception {
        mockMvc.perform(post("/connections")
                .with(csrf())
                .param("email", "jane_doe@mail.com")) //User exists in test database
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index"));
    }

    @Test
    @DisplayName("Given there is no user with email, when add new user connection, then redirect to error page")
    @WithMockUser(username="john_doe@mail.com", roles= "USER")
    public void addUserConnectionFailsTest() throws Exception {
        mockMvc.perform(post("/connections")
                        .with(csrf())
                        .param("email", "no_user@mail.com")) //User doesn't exist in test database
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }
}
