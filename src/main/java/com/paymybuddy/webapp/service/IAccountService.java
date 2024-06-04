package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.Account;

import java.util.Optional;

/**
 * Any class that handles user accounts operations
 */
public interface IAccountService {
    Optional<Account> getUserAccount(int userId);
}
