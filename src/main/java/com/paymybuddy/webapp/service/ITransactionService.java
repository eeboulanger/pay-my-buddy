package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.Transaction;

/**
 * Any class that handles transactions operations
 */
public interface ITransactionService {
    Transaction saveTransaction(Transaction transaction);

    void deleteTransaction(int i);
}
