package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.Account;
import com.paymybuddy.webapp.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService implements IAccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Optional<Account> getUserAccount(int userId) {
        return accountRepository.findById(userId);
    }

    public Account saveAccount(Account newAccount) {
        return accountRepository.save(newAccount);
    }

    public void deleteAccount(int id) {
        accountRepository.deleteById(id);
    }
}
