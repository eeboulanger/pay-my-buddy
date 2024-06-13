package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.exception.UserNotFoundException;
import com.paymybuddy.webapp.model.User;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserConnectionService implements IUserConnectionService {
    private final Logger logger = LoggerFactory.getLogger(UserConnectionService.class);
    @Autowired
    private IAuthenticationFacade authenticationFacade;
    @Autowired
    private UserService userService;

    /**
     * Adds a user connection to the authenticated user if the user connection exists in database
     * @param email is the email of the user connection to be added
     */
    @Override
    @Transactional
    public void addUserConnection(String email) throws UserNotFoundException {
        //Check if user exists in database
        User userConnection = userService.getUserByEmail(email).orElseThrow(() -> {
            logger.error("Failed to create new user connection. No user with email: " + email + " was found.");
            return new UserNotFoundException("No user found with email: " + email);
        });

        User user = getAuthenticatedUser();

        user.getConnections().add(userConnection);
        userService.saveUser(user);
    }

    private User getAuthenticatedUser(){
        Authentication authUser = authenticationFacade.getAuthentication();
        return userService.getUserByEmail(authUser.getName()).orElseThrow(() -> {
            logger.error("Failed to find authenticated user: No user with email " + authUser.getName() + " was found.");
            return new UsernameNotFoundException("Authenticated user not found."); //Force new login
        });
    }
}
