package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private IUserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        return userService.saveUser(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) {
        return userService.saveUser(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users")
    public void deleteUser(@RequestParam("id") int id) {
        userService.deleteById(id);
    }

}
