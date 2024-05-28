package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTests {
    @Mock
    private AccountRepository repository;
    @InjectMocks
    private AccountService service;

    @Test
    public void depositMoneyTest() {

    }

    @Test
    public void transferMoneyTest() {

    }

    @Test
    public void withdrawMoneyTest() {

    }
}
