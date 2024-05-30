package com.paymybuddy.webapp.utils;

/**
 * Any class that handles validation of email and password
 */
public interface IValidationUtil {

    boolean validateEmail(String email);

    boolean validatePassword(String password);
}
