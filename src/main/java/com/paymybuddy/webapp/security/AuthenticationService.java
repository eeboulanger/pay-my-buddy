package com.paymybuddy.webapp.security;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements IAuthenticationService {
    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    @Autowired
    private IUserService userService;

    public User getCurrentUser() {
        Authentication authUser = getAuthentication();
        return userService.getUserByEmail(authUser.getName()).orElseThrow(() -> {
            logger.error("Failed to find authenticated user: No user with email " + authUser.getName() + " was found.");
            return new UsernameNotFoundException("L'utilisateur authentifié n'a pas été trouvé"); //Force login if not found
        });
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
