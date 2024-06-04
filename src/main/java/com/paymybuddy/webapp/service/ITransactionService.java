package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.Transaction;

/**
 * Any class that handles transactions operations
 */
public interface ITransactionService {
    public Transaction saveTransaction(Transaction transaction);
}
