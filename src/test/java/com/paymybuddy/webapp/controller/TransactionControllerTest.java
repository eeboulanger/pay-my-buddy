package com.paymybuddy.webapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.webapp.config.SpringSecurityConfiguration;
import com.paymybuddy.webapp.model.Account;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.repository.UserRepository;
import com.paymybuddy.webapp.service.CustomUserDetailsService;
import com.paymybuddy.webapp.service.IAccountService;
import com.paymybuddy.webapp.service.ITransactionService;
import com.paymybuddy.webapp.service.UserService;
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
import static org.mockito.Mockito.never;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TransactionController.class)
@Import(SpringSecurityConfiguration.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CustomUserDetailsService userDetailsService;
    @MockBean
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ITransactionService transactionService;
    @InjectMocks
    private TransactionController controller;
    private final Transaction transaction = new Transaction();
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("Given authenticated as admin when create new transaction should succeed")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void createNewTransactionAsAdmin_shouldSucceedTest() throws Exception {
        Transaction newTransaction = new Transaction();
        transaction.setAmount(10.00);
        transaction.setDescription("Birthday");

        when(transactionService.saveTransaction(transaction)).thenReturn(newTransaction);

        mockMvc.perform(post("/admin/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(transaction))
                        .with(csrf()))
                .andExpect(status().isOk());
        verify(transactionService, times(1)).saveTransaction(transaction);
    }

    @Test
    @DisplayName("Given authenticated as user when creating a new transaction should fail")
    @WithMockUser(username = "user", roles = "USER")
    public void createNewTransaction_whenAuthenticatedAsUser_shouldFail() throws Exception {
        Transaction newTransaction = new Transaction();
        transaction.setAmount(10.00);
        transaction.setDescription("Birthday");

        when(transactionService.saveTransaction(transaction)).thenReturn(newTransaction);

        mockMvc.perform(post("/admin/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(transaction))
                        .with(csrf()))
                .andExpect(status().is4xxClientError());

        verify(transactionService, never()).saveTransaction(transaction);
    }

    @Test
    @DisplayName("Given authenticated as admin when delete transaction should succeed")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteAsAdmin_shouldSucceedTest() throws Exception {
        mockMvc.perform(delete("/admin/transactions")
                        .param("id", "1")
                        .with(csrf()))
                .andExpect(status().isOk());
        verify(transactionService, times(1)).deleteTransaction(1);
    }

    @Test
    @DisplayName("Given authenticated as user when deleting a transaction should fail")
    @WithMockUser(username = "user", roles = "USER")
    public void delete_whenAuthenticatedAsUser_shouldFail() throws Exception {
        mockMvc.perform(delete("/admin/transactions")
                        .param("id", "1")
                        .with(csrf()))
                .andExpect(status().is4xxClientError());

        verify(transactionService, never()).deleteTransaction(1);
    }
}
