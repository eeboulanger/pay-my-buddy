package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.dto.RegistrationForm;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Any class that handles api for creating an account
 */
public interface IConnexionController {

    @PostMapping("/public/signup")
    String createNewClient(@RequestBody RegistrationForm form);
}
