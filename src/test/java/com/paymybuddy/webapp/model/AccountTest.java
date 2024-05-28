package com.paymybuddy.webapp.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class AccountTest {
    @Test
    public void testAccountProperties() {
        Account account = new Account();

        account.setId(1L);
        account.setBalance(new BigDecimal("1000.00"));

        assertEquals(Long.valueOf(1), account.getId());
        assertEquals(0, account.getBalance().compareTo(new BigDecimal("1000.00")));
    }

    @Test
    public void testAccountEqualsAndHashCode() {
        Account account1 = new Account();
        account1.setId(1L);
        account1.setBalance(new BigDecimal("1000.00"));

        Account account2 = new Account();
        account2.setId(1L);
        account2.setBalance(new BigDecimal("1000.00"));

        assertEquals(account1, account2);
        assertEquals(account1.hashCode(), account2.hashCode());

        account2.setId(2L);
        assertNotEquals(account1, account2);
        assertNotEquals(account1.hashCode(), account2.hashCode());
    }
}
