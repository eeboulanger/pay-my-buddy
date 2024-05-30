package com.paymybuddy.webapp.exception;

public class PasswordNotValidException extends RegistrationException {
    public PasswordNotValidException() {
        super("Password is not valid");
    }
}
