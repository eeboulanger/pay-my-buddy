package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.service.ITransactionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final Logger logger = LoggerFactory.getLogger(TransactionController.class);
    @Autowired
    private ITransactionService transactionService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/{id}")
    public List<Transaction> getTransactionsPerUser(@PathVariable int id) {
        return transactionService.getUserTransactions(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Transaction newTransaction(@Valid @RequestBody Transaction newTransaction) {
        logger.info("Adding new transaction as admin");
        return transactionService.saveTransaction(newTransaction);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public Transaction updateAccount(@Valid @RequestBody Transaction newTransaction) {
        return transactionService.saveTransaction(newTransaction);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable("id") int id) {
        transactionService.deleteTransaction(id);
    }
}
