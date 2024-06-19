package com.paymybuddy.webapp.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TransactionTest {

    private User sender;
    private User receiver;
    private Transaction transaction;
    private static Validator validator;

    @BeforeAll
    public static void setupValidatorInstance() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @BeforeEach
    public void setUp() {
        sender = new User();
        sender.setId(1);
        receiver = new User();
        receiver.setId(2);
        transaction = new Transaction(10, "Gift", Timestamp.from(Instant.now()), sender, receiver);
    }

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

    @Test
    public void whenAmountNotValid_thenConstraintViolations() {
        transaction.setAmount(0); //Not valid
        Set<ConstraintViolation<Transaction>> violations = validator.validateProperty(transaction, "amount");

        assertTrue(violations.size() == 1);
    }

    @Test
    public void whenDescriptionNotValid_thenConstraintViolations() {
        transaction.setDescription(""); //Not valid
        Set<ConstraintViolation<Transaction>> violations = validator.validateProperty(transaction, "description");

        assertTrue(violations.size() == 3); //NotEmpty, MinSize 1, NotBlank
    }

    @Test
    public void whenDoubleNotValid_thenConstraintViolations() {
        transaction.setDate(null); //Not valid
        Set<ConstraintViolation<Transaction>> violations = validator.validateProperty(transaction, "date");

        assertTrue(violations.size() == 1);
    }
}
