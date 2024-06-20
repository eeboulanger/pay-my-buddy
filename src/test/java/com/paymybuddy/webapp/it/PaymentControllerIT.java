package com.paymybuddy.webapp.it;

import com.paymybuddy.webapp.dto.MoneyTransferDTO;
import jakarta.transaction.Transactional;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource("classpath:application-test.properties")
public class PaymentControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Given the request for transfer money is valid, then display success message")
    @WithMockUser(username = "john_doe@mail.com", roles = "USER")
    public void transferMoneyTest() throws Exception {
        MoneyTransferDTO dto = new MoneyTransferDTO();
        dto.setAmount(10.0);
        dto.setReceiverId(2);
        dto.setDescription("Birthday");

        mockMvc.perform(post("/payments")
                        .with(csrf())
                        .param("amount", String.valueOf(dto.getAmount()))
                        .param("description", dto.getDescription())
                        .param("receiverId", String.valueOf(dto.getReceiverId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payments"))
                .andExpect(flash().attribute("message", "success"));
    }

    @Test
    @DisplayName("Given the receiver is not found, then display error message")
    @WithMockUser(username = "john_doe@mail.com", roles = "USER")
    public void transferMoney_whenReceiverNotFound_shouldFail() throws Exception {
        MoneyTransferDTO dto = new MoneyTransferDTO();
        dto.setAmount(10.0);
        dto.setReceiverId(99); //no such receiver id
        dto.setDescription("Birthday");

        mockMvc.perform(post("/payments")
                        .with(csrf())
                        .param("amount", String.valueOf(dto.getAmount()))
                        .param("description", dto.getDescription())
                        .param("receiverId", String.valueOf(dto.getReceiverId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payments"))
                .andExpect(flash().attribute("error", "Le bénéficiaire n'a pas été trouvé"));
    }

    @Test
    @DisplayName("Given the amount is not valid, then display error message")
    @WithMockUser(username = "john_doe@mail.com", roles = "USER")
    public void transferMoney_whenNotValidAmount_shouldFail() throws Exception {
        MoneyTransferDTO dto = new MoneyTransferDTO();
        dto.setAmount(1000000.0);
        dto.setReceiverId(2);
        dto.setDescription("Birthday");

        mockMvc.perform(post("/payments")
                        .with(csrf())
                        .param("amount", String.valueOf(dto.getAmount()))
                        .param("description", dto.getDescription())
                        .param("receiverId", String.valueOf(dto.getReceiverId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payments"))
                .andExpect(flash().attribute("error", "Les fonds sont insuffisants"));
    }

    @Test
    @DisplayName("Given the receiver is not a connection, then show error message")
    @WithMockUser(username = "jimmie_doe@mail.com", roles = "USER")
    public void transferMoney_whenNotConnection_shouldFail() throws Exception {
        MoneyTransferDTO dto = new MoneyTransferDTO();
        dto.setAmount(10);
        dto.setReceiverId(4);//Not yet connected
        dto.setDescription("Birthday");

        mockMvc.perform(post("/payments")
                        .with(csrf())
                        .param("amount", String.valueOf(dto.getAmount()))
                        .param("description", dto.getDescription())
                        .param("receiverId", String.valueOf(dto.getReceiverId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payments"))
                .andExpect(flash().attribute("error", "La relation doit être ajoutée d'abord"));
    }
}
