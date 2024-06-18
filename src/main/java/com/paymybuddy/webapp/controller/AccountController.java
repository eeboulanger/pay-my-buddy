package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.Account;
import com.paymybuddy.webapp.service.IAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    @Autowired
    private IAccountService accountService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/accounts")
    public Account createAccount(@Valid @RequestBody Account newAccount) {
        return accountService.saveAccount(newAccount);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/accounts")
    public Account updateAccount(@Valid @RequestBody Account newAccount) {
        return accountService.saveAccount(newAccount);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/accounts")
    public void deleteAccount(@RequestParam("id") int id) {
        accountService.deleteAccount(id);
    }
}
