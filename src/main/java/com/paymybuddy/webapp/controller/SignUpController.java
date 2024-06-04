package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.dto.RegistrationForm;
import com.paymybuddy.webapp.exception.RegistrationException;
import com.paymybuddy.webapp.service.ISignUpService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class SignUpController {
    private final Logger logger = LoggerFactory.getLogger(SignUpController.class);
    @Autowired
    private ISignUpService service;

    @PostMapping("/users/signup")
    public String createNewUser(@Valid @RequestBody RegistrationForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/error";
        } else {
            try {
                service.signUp(form);
                return "redirect:/index";
            } catch (RegistrationException e) {
                logger.error(e.getMessage());
                return "redirect:/error";
            }
        }
    }
}
