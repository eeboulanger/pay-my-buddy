package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository repository;
    @InjectMocks
    private TransactionService service;

    @Test
    public void getClientTransactionsTest() {

    }
}
