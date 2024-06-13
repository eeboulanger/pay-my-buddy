package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.MoneyTransferDTO;
import com.paymybuddy.webapp.exception.PaymentException;
import com.paymybuddy.webapp.exception.UserNotFoundException;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.User;

import java.util.List;
import java.util.Set;

/**
 * Any class that handles payment between users
 */
public interface IPaymentService {

    void transferMoney(MoneyTransferDTO dto) throws PaymentException, UserNotFoundException;

    List<Transaction> getUserTransactions();

    Set<User> getUserConnections();
}
