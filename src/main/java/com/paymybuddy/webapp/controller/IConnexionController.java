package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.dto.RegistrationForm;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface IConnexionController {

    @PostMapping("/public/signup")
    String createNewClient(@RequestBody RegistrationForm form);
}
