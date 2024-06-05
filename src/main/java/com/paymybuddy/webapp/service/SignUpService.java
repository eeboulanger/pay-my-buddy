package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.UserDTO;
import com.paymybuddy.webapp.exception.RegistrationException;
import com.paymybuddy.webapp.model.User;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignUpService implements ISignUpService {
    private final Logger logger = LoggerFactory.getLogger(SignUpService.class);
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserService service;

    @Autowired
    public SignUpService(BCryptPasswordEncoder passwordEncoder, UserService service) {
        this.passwordEncoder = passwordEncoder;
        this.service = service;
    }

    @Override
    @Transactional
    public void signUp(UserDTO form) throws RegistrationException {
        // Check if the email already exists in the database
        if (service.getUserByEmail(form.getEmail()).isPresent()) {
            logger.error("Failed to create new user. Email already exists in database");
            throw new RegistrationException("Email already exists in database");
        }
        User user = mapToUser(form);
        service.saveUser(user);
    }

    /**
     * create client based on registration form
     */
    private User mapToUser(UserDTO form) {
        User user = new User();
        user.setEmail(form.getEmail());
        user.setUsername(form.getUsername());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        return user;
    }
}
