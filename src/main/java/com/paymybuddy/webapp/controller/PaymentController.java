package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.dto.MoneyTransferDTO;
import com.paymybuddy.webapp.exception.PaymentException;
import com.paymybuddy.webapp.exception.UserNotFoundException;
import com.paymybuddy.webapp.service.IPaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class PaymentController {
    @Autowired
    private IPaymentService paymentService;

    @PostMapping("/transactions")
    public String transferMoney(@Valid @RequestBody MoneyTransferDTO moneyTransfer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/error";
        } else {
            try {
                paymentService.transferMoney(moneyTransfer);
                return "redirect:/index";
            } catch (PaymentException | UserNotFoundException e) {
                return "redirect:/error";
            }
        }
    }
}
