package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.RegistrationForm;
import com.paymybuddy.webapp.exception.RegistrationException;
import com.paymybuddy.webapp.model.Client;

/**
 * Any class that handles connexion to the app
 */
public interface IConnexionService {

    String logIn(String email, String password);

    boolean signUp(RegistrationForm client) throws RegistrationException;
}
