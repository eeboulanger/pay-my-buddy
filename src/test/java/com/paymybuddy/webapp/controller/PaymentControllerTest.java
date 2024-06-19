package com.paymybuddy.webapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.webapp.dto.MoneyTransferDTO;
import com.paymybuddy.webapp.exception.PaymentException;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.service.IPaymentService;
import com.paymybuddy.webapp.service.IUserConnectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PaymentController.class)
public class PaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IUserConnectionService connectionService;
    @MockBean
    private IPaymentService paymentService;
    @InjectMocks
    private PaymentController controller;

    @Nested
    class MoneyTransferTests {
        private MoneyTransferDTO dto;
        private final ObjectMapper mapper = new ObjectMapper();

        @BeforeEach
        public void setUp() {
            dto = new MoneyTransferDTO();
            dto.setAmount(10);
            dto.setReceiverId(22);
            dto.setDescription("Birthday");
        }

        @Test
        @WithMockUser(username = "user", roles = "USER")
        public void transferMoneyTest() throws Exception {
            mockMvc.perform(post("/payments")
                            .with(csrf())
                            .param("receiverId", String.valueOf(dto.getReceiverId()))
                            .param("amount", String.valueOf(dto.getAmount()))
                            .param("description", dto.getDescription()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/payments"));

            verify(paymentService, times(1)).transferMoney(dto);
        }

        @Test
        @WithMockUser(username = "user", roles = "USER")
        public void transferMoneyFailsTest() throws Exception {
            doThrow(PaymentException.class).when(paymentService).transferMoney(dto);

            mockMvc.perform(post("/payments")
                            .with(csrf())
                            .param("receiverId", String.valueOf(dto.getReceiverId()))
                            .param("amount", String.valueOf(dto.getAmount()))
                            .param("description", dto.getDescription()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/payments"));

            verify(paymentService, times(1)).transferMoney(dto);
        }

        @Test
        @WithMockUser(username = "user", roles = "USER")
        public void invalidTransferDetails_shouldReturnError() throws Exception {
            dto.setAmount(0); //invalid amount

            mockMvc.perform(post("/payments")
                            .with(csrf())
                            .param("receiverId", String.valueOf(dto.getReceiverId()))
                            .param("amount", String.valueOf(dto.getAmount()))
                            .param("description", dto.getDescription()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/payments"));
        }

    }

    @Nested
    class UserConnectionsTest {

        private Set<User> connections;

        @BeforeEach
        public void setUp() {
            User user1 = new User();
            user1.setUsername("Joe");
            User user2 = new User();
            user2.setUsername("Jane");

            connections = Set.of(user1, user2);
        }

        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("Given the user has connections, then add connections to model")
        public void getUserConnectionsTest() throws Exception {
            when(paymentService.getUserConnections()).thenReturn(Optional.of(connections));

            mockMvc.perform(get("/payments")
                            .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(model().attribute("connections", connections))
                    .andExpect(view().name("payment"));

            verify(paymentService, times(1)).getUserConnections();
        }
    }

    @Nested
    class UserTransactionsTest {

        private List<Transaction> transactions;

        @BeforeEach
        public void setUp() {
            User user1 = new User();
            user1.setUsername("Joe");
            User user2 = new User();
            user2.setUsername("Jane");
            Transaction transaction1 = new Transaction();
            transaction1.setReceiver(user1);
            Transaction transaction2 = new Transaction();
            transaction2.setReceiver(user2);

            transactions = List.of(transaction1, transaction2);
        }

        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("Given the user has done previous transactions, when add transactions to model")
        public void getMoneyTransactionsTest() throws Exception {
            when(paymentService.getCurrentUserTransactions()).thenReturn(Optional.of(transactions));

            mockMvc.perform(get("/payments")
                            .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(model().attribute("transactions", transactions))
                    .andExpect(view().name("payment"));

            verify(paymentService, times(1)).getUserConnections();
        }
    }
}
