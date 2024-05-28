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

    @Mock
    private Connection mockedConnection;
    @Test
    public void testTransactionGettersAndSetters() {
        // Given
        Transaction transaction = new Transaction();
        String expectedDescription = "Shoes";
        BigDecimal expectedAmount = new BigDecimal("100.00");
        Timestamp expectedDate = Timestamp.from(Instant.now());

        // When
        transaction.setId(1L);
        transaction.setDescription(expectedDescription);
        transaction.setAmount(expectedAmount);
        transaction.setDate(expectedDate);
        transaction.setConnection(mockedConnection);

        // Then
        assertEquals(1L, transaction.getId());
        assertEquals(expectedDescription, transaction.getDescription());
        assertEquals(expectedAmount, transaction.getAmount());
        assertEquals(expectedDate, transaction.getDate());
        assertEquals(mockedConnection, transaction.getConnection());
    }

    @Test
    public void testTransactionEqualsAndHashCode() {
        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setDescription("reimbursement");
        transaction1.setAmount(new BigDecimal("100.00"));
        transaction1.setDate(Timestamp.valueOf("2023-05-01 12:34:56"));
        transaction1.setConnection(mockedConnection);

        Transaction transaction2 = new Transaction();
        transaction2.setId(1L);
        transaction2.setDescription("reimbursement");
        transaction2.setAmount(new BigDecimal("100.00"));
        transaction2.setDate(Timestamp.valueOf("2023-05-01 12:34:56"));
        transaction2.setConnection(mockedConnection);

        assertEquals(transaction1, transaction2);
        assertEquals(transaction1.hashCode(), transaction2.hashCode());

        transaction2.setId(2L);
        assertNotEquals(transaction1, transaction2);
        assertNotEquals(transaction1.hashCode(), transaction2.hashCode());
    }
}
