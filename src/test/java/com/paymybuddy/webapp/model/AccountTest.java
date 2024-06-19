package com.paymybuddy.webapp.model;

import com.paymybuddy.webapp.exception.PaymentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {

    private Account account;

    @BeforeEach
    public void setUp() {
        account = new Account();
        account.setId(1);
        account.setBalance(1000.00);

    }

    @Test
    public void testAccountProperties() {

        assertEquals(Long.valueOf(1), account.getId());
        assertEquals(1000.00, account.getBalance());
    }

    @Test
    public void testAccountEqualsAndHashCode() {
        Account account2 = new Account();
        account2.setId(1);
        account2.setBalance(1000.00);

        assertEquals(account, account2);
        assertEquals(account.hashCode(), account2.hashCode());

        account2.setId(2);
        assertNotEquals(account, account2);
        assertNotEquals(account.hashCode(), account2.hashCode());
    }

    @Test
    public void debitTest() throws PaymentException {
        account.debit(500);
        assertEquals(500.00, account.getBalance());
    }

    @Test
    public void debitTestFails() {
        assertThrows(PaymentException.class, () -> account.debit(1500));
    }

    @Test
    public void creditTest() {
        account.credit(500.50);
        assertEquals(1500.50, account.getBalance());
    }
}
