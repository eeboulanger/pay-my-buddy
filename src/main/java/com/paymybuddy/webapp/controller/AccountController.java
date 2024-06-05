package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.Account;
import com.paymybuddy.webapp.service.IAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    @Autowired
    private IAccountService accountService;

    @PostMapping("/admin/accounts")
    public Account newAccount(@Valid @RequestBody Account newAccount){
        return accountService.saveAccount(newAccount);
    }

    @PutMapping("/admin/accounts")
    public Account updateAccount(@Valid @RequestBody Account newAccount){
        return accountService.saveAccount(newAccount);
    }
    @DeleteMapping("/admin/accounts")
    public void deleteAccount(@RequestParam("id") int id){
        accountService.deleteAccount(id);
    }
}
