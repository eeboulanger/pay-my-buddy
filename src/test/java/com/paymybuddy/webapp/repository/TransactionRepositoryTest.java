package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository repository;

    @Test
    public void getTransactionsByUserIdTest() {

        List<Transaction> result = repository.getTransactionsByUserId(2);

        assertEquals(4, result.size());

        assertEquals("Cadeau à Jimmie", result.getFirst().getDescription());
        assertEquals(15, result.getFirst().getAmount());
        assertEquals("2019-02-12 14:02:56.123456", result.getFirst().getDate().toString());
        assertEquals(1, result.getFirst().getSender().getId());
        assertEquals(2, result.getFirst().getReceiver().getId());

        assertEquals("Billets de cinéma", result.getLast().getDescription());
        assertEquals(8, result.getLast().getAmount());
        assertEquals("2018-11-12 13:02:56.123456", result.getLast().getDate().toString());
        assertEquals(2, result.getLast().getSender().getId());
        assertEquals(1, result.getLast().getReceiver().getId());
    }
}
