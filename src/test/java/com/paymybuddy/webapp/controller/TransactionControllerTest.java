package com.paymybuddy.webapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.webapp.security.CustomOAuth2Service;
import com.paymybuddy.webapp.security.CustomUserDetailsService;
import com.paymybuddy.webapp.security.SpringSecurityConfiguration;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.User;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TransactionController.class)
@Import(SpringSecurityConfiguration.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CustomUserDetailsService userDetailsService;
    @MockBean
    private CustomOAuth2Service oAuth2Service;
    @MockBean
    private IUserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ITransactionService transactionService;
    @InjectMocks
    private TransactionController controller;
    private Transaction transaction;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        Timestamp date = Timestamp.from(Instant.now());
        User receiver = new User();
        receiver.setId(1);
        User sender = new User();
        sender.setId(2);

        transaction = new Transaction();
        transaction.setAmount(10.00);
        transaction.setReceiver(receiver);
        transaction.setSender(sender);
        transaction.setDescription("Birthday");
        transaction.setDate(date);
    }

    @Test
    @DisplayName("Given authenticated as admin when create new transaction should succeed")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void createNewTransactionAsAdmin_shouldSucceedTest() throws Exception {
        when(transactionService.saveTransaction(any(Transaction.class))).thenReturn(transaction);

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(transaction))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value("10.0"))
                .andExpect(jsonPath("$.description").value("Birthday"));

        verify(transactionService, times(1)).saveTransaction(any(Transaction.class));
    }

    @Test
    @DisplayName("Given authenticated as user when creating a new transaction should fail")
    @WithMockUser(username = "user", roles = "USER")
    public void createNewTransaction_whenAuthenticatedAsUser_shouldFail() throws Exception {
        transaction.setAmount(10.00);
        transaction.setDescription("Birthday");

        when(transactionService.saveTransaction(any(Transaction.class))).thenReturn(transaction);

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(transaction))
                        .with(csrf()))
                .andExpect(status().isForbidden());

        verify(transactionService, never()).saveTransaction(any(Transaction.class));
    }

    @Test
    @DisplayName("Given authenticated as admin when delete transaction should succeed")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteAsAdmin_shouldSucceedTest() throws Exception {
        mockMvc.perform(delete("/transactions/{id}", 1)
                        .with(csrf()))
                .andExpect(status().isOk());
        verify(transactionService, times(1)).deleteTransaction(1);
    }

    @Test
    @DisplayName("Given authenticated as user when deleting a transaction should fail")
    @WithMockUser(username = "user", roles = "USER")
    public void delete_whenAuthenticatedAsUser_shouldFail() throws Exception {
        mockMvc.perform(delete("/transactions/{1}", 1)
                        .with(csrf()))
                .andExpect(status().is4xxClientError());

        verify(transactionService, never()).deleteTransaction(1);
    }

    @Test
    @DisplayName("Given the user has done transactions, when get transactions by user, then return list")
    @WithMockUser(roles = "ADMIN")
    public void getTransactionsByUserTest() throws Exception {
        Timestamp date = Timestamp.from(Instant.now());
        User sender = new User();
        sender.setId(1);
        User receiver = new User();
        receiver.setId(2);
        List<Transaction> transactionList = List.of(new Transaction(
                10, "Gift", date, sender, receiver
        ));
        when(transactionService.getTransactionsBySender(1)).thenReturn(transactionList);
        mockMvc.perform(get("/transactions/users/{id}", 1)
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.[0].amount").value(10.0))
                .andExpect(jsonPath("$.[0].description").value("Gift"));
    }
}
