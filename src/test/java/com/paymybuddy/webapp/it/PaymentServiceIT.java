package com.paymybuddy.webapp.it;

import com.paymybuddy.webapp.dto.MoneyTransferDTO;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.TransactionRepository;
import com.paymybuddy.webapp.repository.UserRepository;
import com.paymybuddy.webapp.service.ITransactionService;
import com.paymybuddy.webapp.service.PaymentService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestPropertySource("classpath:application-test.properties")
public class PaymentServiceIT {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    @MockBean
    private ITransactionService transactionService;
    @Autowired
    private PaymentService paymentService;

    @Test
    @DisplayName("Given saving transaction fails, when create a money transfer, then rollback")
    @WithMockUser(username = "john_doe@mail.com", password = "1234@Abcd")
    public void transactionalFailsTest() {
        //Get account balance per user before money transfer
        User sender = userRepository.findById(1).orElse(new User());
        double senderBefore = sender.getAccount().getBalance();
        User receiver = userRepository.findById(2).orElse(new User());
        double receiverBefore = receiver.getAccount().getBalance();

        //GIVEN
        MoneyTransferDTO dto = new MoneyTransferDTO(2, 10, "Test");
        when(transactionService.saveTransaction(any(Transaction.class))).thenThrow(new RuntimeException());

        //WHEN
        assertThrows(RuntimeException.class, () -> paymentService.transferMoney(dto));

        //get account balance after money transfer
        sender = userRepository.findById(1).orElse(new User());
        double senderAfter = sender.getAccount().getBalance();
        receiver = userRepository.findById(2).orElse(new User());
        double receiverAfter = receiver.getAccount().getBalance();
        List<Transaction> transactions = transactionRepository.getTransactionsByUserId(1);

        //THEN
        assertTrue(senderBefore == senderAfter);
        assertTrue(receiverBefore == receiverAfter);
        assertFalse(transactions.stream().anyMatch(transaction -> transaction.getDescription().equals("Test")));
    }
}
