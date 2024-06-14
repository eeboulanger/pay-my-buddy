package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.UserDTO;
import com.paymybuddy.webapp.exception.RegistrationException;
import com.paymybuddy.webapp.model.Account;
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
    private final IUserService service;

    @Autowired
    public SignUpService(BCryptPasswordEncoder passwordEncoder, IUserService service) {
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
        mapToUser(form);
    }

    /**
     * create new user and user account based on registration form
     */
    @Transactional
    private void mapToUser(UserDTO form) {
        User user = new User();
        user.setEmail(form.getEmail());
        user.setUsername(form.getUsername());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user = service.saveUser(user);

        Account account = new Account();
        account.setBalance(0.00);
        account.setUser(user);
        user.setAccount(account);
        service.saveUser(user);
    }
}
