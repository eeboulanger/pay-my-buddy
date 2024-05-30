package com.paymybuddy.webapp.exception;

public class EmailNotValidException extends RegistrationException {
    public EmailNotValidException() {
        super("Email is not valid");
    }
}
