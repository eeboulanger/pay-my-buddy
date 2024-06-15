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

    private final Logger logger = LoggerFactory.getLogger(UserProfileService.class);
    @Autowired
    private IAuthenticationFacade authenticationFacade;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private IUserService userService;

    /**
     * Checks if user details email, password and username have been updated and saves the details to database
     * @param updatedUser are the user details retrieved from the form
     */
    @Override
    @Transactional
    public void updateUser(UserDTO updatedUser) {
        User user = getAuthUser();

        if (!updatedUser.getEmail().equals(user.getEmail())) {
            logger.info("Updating user email");
            user.setEmail(updatedUser.getEmail());
        }
        if (!passwordEncoder.matches(updatedUser.getPassword(), user.getPassword())) {
            logger.info("Updating user password");
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        if (!updatedUser.getUsername().equals(user.getUsername())) {
            logger.info("Updating username");
            user.setUsername(updatedUser.getUsername());
        }
        userService.saveUser(user);
    }

    /**
     *
     * @return user details email and user name
     */
    public UserDTO getCurrentUser() {
        UserDTO dto = new UserDTO();
        User user = getAuthUser();

        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        return dto;
    }

    /**
     * Get user details for the authenticated user
     * @return
     */
    private User getAuthUser() {
        Authentication authUser = authenticationFacade.getAuthentication();
        logger.debug("Get current authenticated user: " + authUser.getName());
        return userService.getUserByEmail(authUser.getName()).orElseThrow(() -> {
            logger.error("Failed to find authenticated user: No user with email " + authUser.getName() + " was found.");
            return new UsernameNotFoundException("Utilisateur authentifié non trouvé"); //Force new login
        });
    }
}
