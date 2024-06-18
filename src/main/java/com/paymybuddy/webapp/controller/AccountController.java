package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.Account;
import com.paymybuddy.webapp.service.IAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private IAccountService accountService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Account createAccount(@Valid @RequestBody Account newAccount) {
        return accountService.saveAccount(newAccount);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public Account updateAccount(@Valid @RequestBody Account newAccount) {
        return accountService.saveAccount(newAccount);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable("id") int id) {
        accountService.deleteAccount(id);
    }
}
