package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository repository;
    @InjectMocks
    private TransactionService service;

    @Test
    public void saveTransactionTest() {
        Transaction transaction = new Transaction();
        when(repository.save(transaction)).thenReturn(transaction);

        Transaction result = service.saveTransaction(transaction);

        assertNotNull(result);
        assertEquals(transaction, result);
        verify(repository, times(1)).save(transaction);

    }

    @Test
    public void deleteTransactionTest() {
        Transaction transaction = new Transaction();
        transaction.setId(1);

        service.deleteTransaction(1);

        verify(repository, times(1)).deleteById(1);
    }

    @Test
    public void getUserTransactionTest() {
        List<Transaction> transactionList = new ArrayList<>();
        when(repository.getTransactionsByUserId(1)).thenReturn(transactionList);

        List<Transaction> result = service.getUserTransactions(1);

        assertNotNull(result);
        assertEquals(transactionList, result);
        verify(repository, times(1)).getTransactionsByUserId(1);
    }
}
