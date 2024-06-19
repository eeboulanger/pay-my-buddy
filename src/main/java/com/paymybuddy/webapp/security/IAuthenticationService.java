package com.paymybuddy.webapp.security;

import com.paymybuddy.webapp.model.User;

/**
 * Get current authenticated user
 */
public interface IAuthenticationService {
    User getCurrentUser();
}
