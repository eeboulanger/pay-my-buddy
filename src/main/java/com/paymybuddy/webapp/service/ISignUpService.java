package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.UserDTO;
import com.paymybuddy.webapp.exception.RegistrationException;

/**
 * Any class that handles sign up
 */
public interface ISignUpService {
    void signUp(UserDTO client) throws RegistrationException;
}
