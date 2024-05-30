package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.RegistrationForm;
import com.paymybuddy.webapp.exception.EmailNotUniqueException;
import com.paymybuddy.webapp.exception.EmailNotValidException;
import com.paymybuddy.webapp.exception.PasswordNotValidException;
import com.paymybuddy.webapp.exception.RegistrationException;
import com.paymybuddy.webapp.model.Client;
import com.paymybuddy.webapp.utils.IValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ConnexionService implements IConnexionService {
    private final Logger logger = LoggerFactory.getLogger(ConnexionService.class);
    @Autowired
    private IValidationUtil validationUtil;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private ClientService service;

    @Override
    public String logIn(String email, String password) {
        return "error";
    }

    @Override
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
