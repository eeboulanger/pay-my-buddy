package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.dto.RegistrationForm;
import com.paymybuddy.webapp.exception.RegistrationException;
import com.paymybuddy.webapp.service.ISignUpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ConnexionController implements IConnexionController {
    private final Logger logger = LoggerFactory.getLogger(ConnexionController.class);
    @Autowired
    private ISignUpService service;

    @PostMapping("/public/signup")
    public String createNewClient(@RequestBody RegistrationForm form) {
        try {
            if (service.signUp(form)) {
                return "redirect:/index";
            }
        } catch (RegistrationException e) {
            logger.error(e.getMessage());
        }
        return "redirect:/error";
    }
}
