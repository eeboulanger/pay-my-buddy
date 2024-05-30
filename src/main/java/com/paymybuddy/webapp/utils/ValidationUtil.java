package com.paymybuddy.webapp.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ValidationUtil implements IValidationUtil {

    /**
     * Using OWASP Regex validation
     *
     * @param email to test
     * @return true if valid email
     */
    @Override
    public boolean validateEmail(String email) {
        String regexPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }

    /**
     *  password should :
     *         //Have eight characters or more
     *         //Include a capital letter
     *         //Use at least one lowercase letter
     *         //Consists of at least one digit
     *         //Need to have one special symbol (i.e., @, #, $, %, etc.)
     *         //Doesn’t contain space, tab, etc.
     * @param password to test
     * @return true if valid
     */
    @Override
    public boolean validatePassword(String password) {
        String regexPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";

        return Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE)
                .matcher(password)
                .matches();
    }
}
