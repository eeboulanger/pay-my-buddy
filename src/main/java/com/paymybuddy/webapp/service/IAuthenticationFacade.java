package com.paymybuddy.webapp.service;

import org.springframework.security.core.Authentication;

/**
 * Any class that handles retrieving authenticated user info
 */

public interface IAuthenticationFacade {
    Authentication getAuthentication();
}
