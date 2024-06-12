package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.service.ITransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {

    @Autowired
    private ITransactionService transactionService;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/transactions")
    public Transaction newTransaction(@Valid @RequestBody Transaction newTransaction) {
        return transactionService.saveTransaction(newTransaction);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/transactions")
    public Transaction updateAccount(@Valid @RequestBody Transaction newTransaction) {
        return transactionService.saveTransaction(newTransaction);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/transactions")
    public void deleteAccount(@RequestParam("id") int id) {
        transactionService.deleteTransaction(id);
    }
}
