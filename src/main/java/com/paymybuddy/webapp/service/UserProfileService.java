package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.UserDTO;
import com.paymybuddy.webapp.exception.ProfileException;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.security.IAuthenticationService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileService implements IUserProfileService {

    private final Logger logger = LoggerFactory.getLogger(UserProfileService.class);
    @Autowired
    private IAuthenticationService authenticationService;
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
    public void updateUser(UserDTO updatedUser) throws ProfileException {
        User user = authenticationService.getCurrentUser();

        if (!updatedUser.getEmail().equals(user.getEmail())) {
            logger.info("Updating user email");
            checkIfEmailExists(updatedUser.getEmail());
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

    private void checkIfEmailExists(String newEmail) throws ProfileException {
        Optional<User> optionalUser = userService.getUserByEmail(newEmail);
        if(optionalUser.isPresent()){
            throw new ProfileException("L'email est déjà utilisé");
        }
    }

    /**
     *
     * @return user details email and user name
     */
    public UserDTO getCurrentUserAsDTO() {
        UserDTO dto = new UserDTO();
        User user = authenticationService.getCurrentUser();

        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        return dto;
    }
}
