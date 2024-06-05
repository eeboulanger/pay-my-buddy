package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.dto.RegistrationForm;
import com.paymybuddy.webapp.exception.RegistrationException;
import com.paymybuddy.webapp.service.ISignUpService;
import com.paymybuddy.webapp.service.IUserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserProfileController {
    private final Logger logger = LoggerFactory.getLogger(UserProfileController.class);
    @Autowired
    private ISignUpService service;
    @Autowired
    private IUserService userService;

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

    @DeleteMapping("/admin/users")
    public void deleteUser(@RequestParam("id") int id){
        userService.deleteById(id);
    }
}
