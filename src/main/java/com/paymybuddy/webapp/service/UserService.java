package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private ClientRepository clientRepository;

    public Optional<User> getUserByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        return clientRepository.save(user);
    }
}
