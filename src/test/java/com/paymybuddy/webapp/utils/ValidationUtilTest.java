package com.paymybuddy.webapp.utils;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidationUtilTest {
    private final ValidationUtil util = new ValidationUtil();

    @Test
    public void testValidateEmailSuccess() {
        String emailAddress = "username@domain.com";

        boolean result = util.validateEmail(emailAddress);
        assertTrue(result);
    }

    @Test
    public void testValidateEmail_whenNoDot_shouldFail() {
        String emailAddress = "username@domaincom";

        boolean result = util.validateEmail(emailAddress);
        assertFalse(result);
    }

    @Test
    public void testValidateEmail_whenNoAt_shouldFail() {
        String emailAddress = "usernamedomain.com";

        boolean result = util.validateEmail(emailAddress);
        assertFalse(result);
    }

    @Test
    public void givenStringPassword_whenUsingRegulaExpressions_thenCheckIfPasswordValid() {
        String password = "AbCD123@";

        boolean result = util.validatePassword(password);

        assertTrue(result);
    }
    @Test
    public void givenPasswordContainsNoDigits_whenUsingRegulaExpressions_thenPasswordIsInValid() {
        String password = "AbCDefg@";

        boolean result = util.validatePassword(password);

        assertFalse(result);
    }
}

