package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void createTransactionTest() {
        User sender = new User();
        sender.setId(1);
        User receiver = new User();
        receiver.setId(2);

        Timestamp date = Timestamp.from(Instant.now());
        Transaction transaction = new Transaction(5.00, "Gift", date, sender, receiver);

        Transaction result = repository.save(transaction);
        assertEquals(5, result.getAmount());
        assertEquals("Gift", result.getDescription());
        assertEquals(date, result.getDate());
        assertEquals(1, result.getSender().getId());
        assertEquals(2, result.getReceiver().getId());
    }

    @Test
    public void getUserTransactions() {
        //GIVEN
        Timestamp date = Timestamp.from(Instant.now());
        User sender = new User();
        sender.setId(1);
        User receiver = new User();
        receiver.setId(2);
        Transaction transaction = new Transaction(2, "Gift", date, sender, receiver);
        repository.save(transaction);

        //WHEN
        List<Transaction> result = repository.getTransactionsByUserId(1);

        //THEN
        assertTrue(result.contains(transaction));
    }

    @Nested
    class ValidationTests {

        private Transaction transaction;
        @BeforeEach
        public void setUp(){
           User sender = new User();
            sender.setId(1);
            User receiver = new User();
            receiver.setId(2);
            transaction = new Transaction(10, "Gift", Timestamp.from(Instant.now()), sender, receiver);

        }
        @Test
        public void whenDescriptionNull_thenFailSave() {
            transaction.setDescription(null);

            assertThrows(Exception.class, () -> repository.save(transaction));
        }

        @Test
        public void whenSenderNull_thenFailSave() {
            transaction.setSender(null);

            assertThrows(Exception.class, () -> repository.save(transaction));
        }
        @Test
        public void whenReceiverNull_thenFailSave() {
            transaction.setReceiver(null);

            assertThrows(Exception.class, () -> repository.save(transaction));
        }
        @Test
        public void whenDateNull_thenFailSave() {
            transaction.setDate(null);

            assertThrows(Exception.class, () -> repository.save(transaction));
        }
    }
}
