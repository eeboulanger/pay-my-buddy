package com.paymybuddy.webapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.webapp.config.SpringSecurityConfiguration;
import com.paymybuddy.webapp.model.Account;
import com.paymybuddy.webapp.repository.UserRepository;
import com.paymybuddy.webapp.service.CustomOAuth2Service;
import com.paymybuddy.webapp.service.CustomUserDetailsService;
import com.paymybuddy.webapp.service.IAccountService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountController.class)
@Import({SpringSecurityConfiguration.class})
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CustomUserDetailsService userDetailsService;
    @MockBean
    private CustomOAuth2Service oAuth2Service;
    @MockBean
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private IAccountService accountService;
    @InjectMocks
    private AccountController controller;
    private Account account;
    private final ObjectMapper mapper = new ObjectMapper();
    @BeforeEach
    public void setUp(){
        account = new Account();
    }

    @Test
    @DisplayName("Given authenticated as admin when create new account should succeed")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void createNewAccountAsAdmin_shouldSucceedTest() throws Exception {
        when(accountService.saveAccount(account)).thenReturn(account);

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(account))
                        .with(csrf()))
                .andExpect(status().isOk());
        verify(accountService, times(1)).saveAccount(account);
    }

    @Test
    @DisplayName("Given authenticated as user when creating a new account should fail")
    @WithMockUser(username = "user", roles = "USER")
    public void createNewAccount_whenAuthenticatedAsUser_shouldFail() throws Exception {
        when(accountService.saveAccount(account)).thenReturn(account);

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(account))
                        .with(csrf()))
                .andExpect(status().is4xxClientError());
        verify(accountService, never()).saveAccount(account);
    }

    @Test
    @DisplayName("Given authenticated as admin when delete new account should succeed")
    @WithMockUser(roles="ADMIN")
    public void deleteAccountAsAdmin_shouldSucceedTest() throws Exception {
        mockMvc.perform(delete("/accounts")
                        .param("id", "1")
                        .with(csrf()))
                .andExpect(status().isOk());
        verify(accountService, times(1)).deleteAccount(1);
    }

    @Test
    @DisplayName("Given authenticated as user when deleting an account should fail")
    @WithMockUser(username = "user", roles = "USER")
    public void deleteAccount_whenAuthenticatedAsUser_shouldFail() throws Exception {
        mockMvc.perform(delete("/accounts")
                        .param("id", "1")
                        .with(csrf()))
                .andExpect(status().is4xxClientError());

        verify(accountService, never()).deleteAccount(1);
    }

    @Test
    @DisplayName("Given authenticated as admin when update account should succeed")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void updateAccountAsAdmin_shouldSucceedTest() throws Exception {
        account.setId(1);
        account.setBalance(10.00);
        when(accountService.saveAccount(account)).thenReturn(account);

        mockMvc.perform(put("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(account))
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(accountService, times(1)).saveAccount(account);
    }

    @Test
    @DisplayName("Given authenticated as user when update an account should fail")
    @WithMockUser(username = "user", roles = "USER")
    public void updateAccount_whenAuthenticatedAsUser_shouldFail() throws Exception {
        when(accountService.saveAccount(account)).thenReturn(account);

        mockMvc.perform(put("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(account))
                        .with(csrf()))
                .andExpect(status().is4xxClientError());

        verify(accountService, never()).saveAccount(account);
    }
}
