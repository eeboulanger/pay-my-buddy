package com.paymybuddy.webapp.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class AccountTest {
    @Test
    public void testAccountProperties() {
        Account account = new Account();

        account.setId(1);
        account.setBalance(1000.00);

        assertEquals(Long.valueOf(1), account.getId());
        assertEquals(1000.00, account.getBalance());
    }

    @Test
    public void testAccountEqualsAndHashCode() {
        Account account1 = new Account();
        account1.setId(1);
        account1.setBalance(1000.00);

        Account account2 = new Account();
        account2.setId(1);
        account2.setBalance(1000.00);

        assertEquals(account1, account2);
        assertEquals(account1.hashCode(), account2.hashCode());

        account2.setId(2);
        assertNotEquals(account1, account2);
        assertNotEquals(account1.hashCode(), account2.hashCode());
    }
}
