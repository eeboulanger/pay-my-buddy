package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.RegistrationForm;
import com.paymybuddy.webapp.exception.EmailNotUniqueException;
import com.paymybuddy.webapp.exception.EmailNotValidException;
import com.paymybuddy.webapp.exception.PasswordNotValidException;
import com.paymybuddy.webapp.exception.RegistrationException;
import com.paymybuddy.webapp.model.Client;
import com.paymybuddy.webapp.utils.IValidationUtil;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignUpService implements ISignUpService {
    private final Logger logger = LoggerFactory.getLogger(SignUpService.class);
    private final IValidationUtil validationUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ClientService service;

    @Autowired
    public SignUpService(IValidationUtil validationUtil, BCryptPasswordEncoder passwordEncoder, ClientService service) {
        this.validationUtil = validationUtil;
        this.passwordEncoder = passwordEncoder;
        this.service = service;
    }

    @Override
    @Transactional
    public boolean signUp(RegistrationForm form) throws RegistrationException {
        // Validate form
        if (!validationUtil.validateEmail(form.getEmail())) {
            logger.error("Email is not valid");
            throw new EmailNotValidException();
        }
        if (!validationUtil.validatePassword(form.getPassword())) {
            logger.error("Password is not valid");
            throw new PasswordNotValidException();
        }
        // Check if the email already exists in the database
        if (!service.getClientByEmail(form.getEmail()).isEmpty()) {
            logger.error("Failed to create new client. Email already exists in database");
            throw new EmailNotUniqueException();
        }

        Client client = mapToClient(form);
        service.createClient(client);
        return true;
    }

    /**
     * create client based on registration form
     */
    private Client mapToClient(RegistrationForm form) {
        Client client = new Client();
        client.setEmail(form.getEmail());
        client.setFirstName(form.getFirstName());
        client.setLastName(form.getLastName());
        client.setPassword(passwordEncoder.encode(form.getPassword()));
        return client;
    }
}
