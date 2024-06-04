package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.MoneyTransferDTO;
import com.paymybuddy.webapp.exception.PaymentException;
import com.paymybuddy.webapp.exception.UserNotFoundException;

/**
 * Any class that handles payment between users
 */
public interface IPaymentService {

    void transferMoney(MoneyTransferDTO dto) throws PaymentException, UserNotFoundException;
}
