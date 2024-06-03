package com.paymybuddy.webapp.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface IUserConnectionController {

    @PostMapping("/connections")
    String addUserConnection(@RequestParam String email);
}
