package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.UserDTO;
import com.paymybuddy.webapp.model.User;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService implements IUserProfileService {

    private final Logger logger = LoggerFactory.getLogger(UserConnectionService.class);
    @Autowired
    private IAuthenticationFacade authenticationFacade;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private IUserService userService;

    @Override
    @Transactional
    public void updateUser(UserDTO updatedUser) {
        logger.debug("Retrieving authenticated user details");

        Authentication authUser = authenticationFacade.getAuthentication();
        User user = userService.getUserByEmail(authUser.getName()).orElseThrow(() -> {
            logger.error("Failed to find authenticated user: No user with email " + authUser.getName() + " was found.");
            return new UsernameNotFoundException("Authenticated user not found."); //Force new login
        });

        if (!updatedUser.getEmail().equals(user.getEmail())) {
            logger.debug("Updating user email");
            user.setEmail(updatedUser.getEmail());
        }
        if (!passwordEncoder.matches(updatedUser.getPassword(), user.getPassword())) {
            logger.debug("Updating user password");
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        if (!updatedUser.getUsername().equals(user.getUsername())) {
            logger.debug("Updating username");
            user.setUsername(updatedUser.getUsername());
        }
        userService.saveUser(user);
    }

    public void deleteUser(int id) {
        logger.debug("Deleting user with id: " + id);
        userService.deleteById(id);
    }
}
