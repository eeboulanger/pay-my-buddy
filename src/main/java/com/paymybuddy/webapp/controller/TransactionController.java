package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.service.ITransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {

    @Autowired
    private ITransactionService transactionService;

    @PostMapping("/admin/transactions")
    public Transaction newTransaction(@Valid @RequestBody Transaction newTransaction){
        return transactionService.saveTransaction(newTransaction);
    }

    @PutMapping("/admin/transactions")
    public Transaction updateAccount(@Valid @RequestBody Transaction newTransaction){
        return transactionService.saveTransaction(newTransaction);
    }

    @DeleteMapping("/admin/transactions")
    public void deleteAccount(@RequestParam("id") int id){
        transactionService.deleteTransaction(id);
    }
}
