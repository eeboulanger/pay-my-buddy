package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.exception.UserNotFoundException;
import com.paymybuddy.webapp.service.IUserConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserConnectionController {
    @Autowired
    private IUserConnectionService service;

    @GetMapping("/connections")
    public String getConnections(Model model){

        return "connection";
    }

    @PostMapping("/connections")
    public String addUserConnection(@RequestParam String email, RedirectAttributes attributes) {
        try {
            service.addUserConnection(email);
            return "redirect:/connections";
        } catch (UserNotFoundException e) {
            attributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/connections";
        }
    }
}
