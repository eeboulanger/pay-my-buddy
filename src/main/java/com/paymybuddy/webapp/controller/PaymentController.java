package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.dto.MoneyTransferDTO;
import com.paymybuddy.webapp.exception.PaymentException;
import com.paymybuddy.webapp.exception.UserNotFoundException;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.service.IPaymentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;

@Controller
public class PaymentController {

    private Logger logger = LoggerFactory.getLogger(PaymentController.class);
    @Autowired
    private IPaymentService paymentService;

    @GetMapping("/payments")
    public String getMoneyTransactions(Model model) {
        Set<User> connections = paymentService.getUserConnections();
        List<Transaction> transactions = paymentService.getUserTransactions();

        model.addAttribute("transactions", transactions);
        model.addAttribute("connections", connections);
        model.addAttribute("moneyTransfer", new MoneyTransferDTO());
        return "payment";
    }

    @PostMapping("/payments")
    public String transferMoney(@Valid @ModelAttribute MoneyTransferDTO moneyTransfer, BindingResult bindingResult, RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            logger.error("Binding result has errors: " + bindingResult.getAllErrors());
            return "redirect:/payments";
        } else {
            try {
                paymentService.transferMoney(moneyTransfer);
                logger.info("Money transfer successfully executed");
                attributes.addFlashAttribute("message", "Success");
                return "redirect:/payments";
            } catch (PaymentException | UserNotFoundException e) {
                logger.error("Money transfer failed with error: " + e.getMessage());
                attributes.addFlashAttribute("error", e.getMessage());
                return "redirect:/payments";
            }
        }
    }
}
