package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.Transaction;

import java.util.List;

/**
 * Any class that handles transactions operations
 */
public interface ITransactionService {
    Transaction saveTransaction(Transaction transaction);

    void deleteTransaction(int i);

    List<Transaction> getUserTransactions(int id);

    List<Transaction> getTransactionsBySender(int id);
}
