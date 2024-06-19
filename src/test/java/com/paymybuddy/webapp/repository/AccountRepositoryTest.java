package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.exception.PaymentException;
import com.paymybuddy.webapp.model.Account;
import com.paymybuddy.webapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository repository;

    @Test
    public void getAccountTest() {
        Optional<Account> result = repository.findById(1);

        assertTrue(result.isPresent());
        assertEquals(1000.00, result.get().getBalance());
        assertEquals("John", result.get().getUser().getUsername());
    }

    @Test
    public void saveAccountTest() throws PaymentException {
        //GIVEN
        Optional<Account> account = repository.findById(1);
        assertTrue(account.isPresent());
        account.get().debit(100);

        //WHEN
        Account result = repository.save(account.get());

        //THEN
        assertEquals(1, result.getId());
        assertEquals(900.00, result.getBalance());
        assertEquals("John", result.getUser().getUsername());
    }

    @Test
    public void deleteAccountTest() {
        repository.deleteById(1);

        Optional<Account> result = repository.findById(1);
        assertTrue(result.isEmpty());
    }

    @Nested
    class ValidationTests {
        private Account account;

        @BeforeEach
        public void setUp() {
            User user = new User();
            user.setId(1);

            account = new Account();
            account.setUser(user);
        }

        @Test
        public void whenUserIdNull_thenFailToSave() {
            //Given user id is null
            account.setUser(null);

            assertThrows(Exception.class, () -> repository.save(account));
        }
    }
}
