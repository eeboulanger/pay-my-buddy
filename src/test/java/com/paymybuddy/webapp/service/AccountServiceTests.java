package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.Account;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTests {
    @Mock
    private AccountRepository repository;
    @InjectMocks
    private AccountService service;


    @Test
    @DisplayName("Given there is an account for user, then return account")
    public void getUserAccountTest() {
        Account account = new Account();
        when(repository.findById(1)).thenReturn(Optional.of(account));

        Optional<Account> optional = service.getUserAccount(1);

        assertTrue(optional.isPresent());
        assertEquals(account, optional.get());
        verify(repository, times(1)).findById(1);
    }
    @Test
    @DisplayName("Given there is no account for user, then return empty")
    public void getUserAccount_whenNoUser_shouldReturnEmpty() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        Optional<Account> optional = service.getUserAccount(1);

        assertTrue(optional.isEmpty());
        verify(repository, times(1)).findById(1);
    }


}
