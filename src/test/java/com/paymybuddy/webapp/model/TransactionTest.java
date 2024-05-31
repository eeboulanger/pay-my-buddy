package com.paymybuddy.webapp.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
public class TransactionTest {

    private final User sender = new User();
    private final User receiver = new User();

    @Test
    public void testTransactionGettersAndSetters() {
        // Given
        Transaction transaction = new Transaction();
        String expectedDescription = "Shoes";
        Double expectedAmount = 100.00;
        Timestamp expectedDate = Timestamp.from(Instant.now());

        // When
        transaction.setId(1);
        transaction.setDescription(expectedDescription);
        transaction.setAmount(expectedAmount);
        transaction.setDate(expectedDate);
        transaction.setReceiver(receiver);
        transaction.setSender(sender);

        // Then
        assertEquals(1, transaction.getId());
        assertEquals(expectedDescription, transaction.getDescription());
        assertEquals(expectedAmount, transaction.getAmount());
        assertEquals(expectedDate, transaction.getDate());
        assertEquals(sender, transaction.getSender());
        assertEquals(receiver, transaction.getReceiver());
    }

    @Test
    public void testTransactionEqualsAndHashCode() {
        Transaction transaction1 = new Transaction();
        transaction1.setId(1);
        transaction1.setDescription("reimbursement");
        transaction1.setAmount(100.00);
        transaction1.setDate(Timestamp.valueOf("2023-05-01 12:34:56"));
        transaction1.setReceiver(receiver);
        transaction1.setSender(sender);


        Transaction transaction2 = new Transaction();
        transaction2.setId(1);
        transaction2.setDescription("reimbursement");
        transaction2.setAmount(100.00);
        transaction2.setDate(Timestamp.valueOf("2023-05-01 12:34:56"));
        transaction2.setReceiver(receiver);
        transaction2.setSender(sender);


        assertEquals(transaction1, transaction2);
        assertEquals(transaction1.hashCode(), transaction2.hashCode());

        transaction2.setId(2);
        assertNotEquals(transaction1, transaction2);
        assertNotEquals(transaction1.hashCode(), transaction2.hashCode());
    }
}
