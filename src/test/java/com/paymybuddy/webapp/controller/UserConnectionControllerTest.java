package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.exception.UserNotFoundException;
import com.paymybuddy.webapp.service.UserConnectionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserConnectionController.class)
public class UserConnectionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserConnectionService service;
    @InjectMocks
    private UserConnectionController controller;
    private final String email = "jane@mail.com";

    @Test
    @DisplayName("Given user connection is found then add connection and show success message")
    @WithMockUser(username="user", roles= "USER")
    public void addUserConnectionTest() throws Exception {
        mockMvc.perform(post("/connections")
                        .with(csrf())
                        .param("email", email))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/connections"))
                .andExpect(flash().attribute("success", true));

        verify(service, times(1)).addUserConnection(email);

    }

    @Test
    @WithMockUser(username="user", roles= "USER")
    public void addUserConnectionFailsTest() throws Exception {
        doThrow(new UserNotFoundException("User not found")).when(service).addUserConnection(email);

        mockMvc.perform(post("/connections")
                        .with(csrf())
                        .param("email",email))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/connections"));

        verify(service, times(1)).addUserConnection(email);
    }
}
