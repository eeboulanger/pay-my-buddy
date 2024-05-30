package com.paymybuddy.webapp.exception;

public class EmailNotUniqueException extends RegistrationException {
    public EmailNotUniqueException() {
        super("Provide a unique email");
    }
}
