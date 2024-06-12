package com.paymybuddy.webapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.webapp.dto.MoneyTransferDTO;
import com.paymybuddy.webapp.exception.PaymentException;
import com.paymybuddy.webapp.service.IPaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PaymentController.class)
public class PaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPaymentService paymentService;
    @InjectMocks
    private PaymentController controller;
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
        mockMvc.perform(post("/payment")
                        .with(csrf())
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index"));

        verify(paymentService, times(1)).transferMoney(dto);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void transferMoneyFailsTest() throws Exception {
        doThrow(PaymentException.class).when(paymentService).transferMoney(dto);

        mockMvc.perform(post("/payment")
                        .with(csrf())
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));

        verify(paymentService, times(1)).transferMoney(dto);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void invalidTransferDetails_shouldReturnError() throws Exception {
       dto.setAmount(0); //invalid amount

        mockMvc.perform(post("/payment")
                        .with(csrf())
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }
}
