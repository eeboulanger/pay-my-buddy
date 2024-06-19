package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.exception.UserNotFoundException;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.security.IAuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserConnectionService implements IUserConnectionService {
    private final Logger logger = LoggerFactory.getLogger(UserConnectionService.class);
    @Autowired
    private IAuthenticationService authenticationService;
    @Autowired
    private UserService userService;

    /**
     * Adds a user connection to the authenticated user if the user connection exists in database
     *
     * @param email is the email of the user connection to be added
     */
    @Override
    public void addUserConnection(String email) throws UserNotFoundException {
        //Check if user exists in database
        User userConnection = userService.getUserByEmail(email).orElseThrow(() -> {
            logger.error("Failed to create new user connection. No user with email: " + email + " was found.");
            return new UserNotFoundException("Aucun utilisateur trouvé avec email: " + email);
        });

        User user = authenticationService.getCurrentUser();
        if (email.equals(user.getEmail())) {
            logger.error("Failed to create new user connection. The email: " + email + " is identical to the current users email");
            throw new UserNotFoundException("L'addresse mail doit être différent de la votre");
        }

        if(user.getConnections().contains(userConnection)){
            throw new UserNotFoundException("La relation a déjà été ajoutée");
        }
        user.getConnections().add(userConnection);
        userService.saveUser(user);
    }

}
