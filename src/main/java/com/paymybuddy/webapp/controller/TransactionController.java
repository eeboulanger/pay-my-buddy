package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.service.ITransactionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TransactionController {

    private final Logger logger = LoggerFactory.getLogger(TransactionController.class);
    @Autowired
    private ITransactionService transactionService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/transactions")
    public Transaction newTransaction(@Valid @RequestBody Transaction newTransaction) {
        logger.info("Adding new transaction as admin");
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
