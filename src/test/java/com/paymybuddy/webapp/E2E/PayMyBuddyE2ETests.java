package com.paymybuddy.webapp.E2E;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.webapp.dto.MoneyTransferDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
    @WithMockUser(username = "john_doe@mail.com", roles = "USER")
    public void addUserConnectionTest() throws Exception {
        mockMvc.perform(post("/connections")
                        .with(csrf())
                        .param("email", "jane_doe@mail.com")) //User exists in test database
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index"));
    }

    @Test
    @DisplayName("Given there is no user with email, when add new user connection, then redirect to error page")
    @WithMockUser(username = "john_doe@mail.com", roles = "USER")
    public void addUserConnectionFailsTest() throws Exception {
        mockMvc.perform(post("/connections")
                        .with(csrf())
                        .param("email", "no_user@mail.com")) //User doesn't exist in test database
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }

    @Test
    @DisplayName("Given the request for transfer money is valid, then redirect to index page")
    @WithMockUser(username = "john_doe@mail.com", roles = "USER")
    public void transferMoneyTest() throws Exception {
        MoneyTransferDTO dto = new MoneyTransferDTO();
        dto.setAmount(10.0);
        dto.setReceiverId(2);
        dto.setDescription("Birthday");

        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(post("/transactions")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index"));
    }

    @Test
    @DisplayName("Given the receiver is not found, then redirect to error page")
    @WithMockUser(username = "john_doe@mail.com", roles = "USER")
    public void transferMoney_whenReceiverNotFound_shouldFail() throws Exception {
        MoneyTransferDTO dto = new MoneyTransferDTO();
        dto.setAmount(10.0);
        dto.setReceiverId(99);
        dto.setDescription("Birthday");

        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(post("/transactions")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }

    @Test
    @DisplayName("Given the amount is not valid, then redirect to error page")
    @WithMockUser(username = "john_doe@mail.com", roles = "USER")
    public void transferMoney_whenNotValidAmount_shouldFail() throws Exception {
        MoneyTransferDTO dto = new MoneyTransferDTO();
        dto.setAmount(1000000.0);
        dto.setReceiverId(2);
        dto.setDescription("Birthday");

        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(post("/transactions")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }

    @Test
    @DisplayName("Given the receiver is not a connection, then redirect to error page")
    @WithMockUser(username = "john_doe@mail.com", roles = "USER")
    public void transferMoney_whenNotConnection_shouldFail() throws Exception {
        MoneyTransferDTO dto = new MoneyTransferDTO();
        dto.setAmount(10);
        dto.setReceiverId(3);
        dto.setDescription("Birthday");

        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(post("/transactions")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }
}
