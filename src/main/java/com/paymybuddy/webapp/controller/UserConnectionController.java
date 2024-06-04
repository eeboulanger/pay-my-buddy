package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.exception.UserNotFoundException;
import com.paymybuddy.webapp.service.IUserConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserConnectionController {
    @Autowired
    private IUserConnectionService service;

    @PostMapping("/connections")
    public String addUserConnection(@RequestParam String email) {
        try {
            service.addUserConnection(email);
            return "redirect:/index";
        } catch (UserNotFoundException e) {
            return "redirect:/error";
        }
    }
}
