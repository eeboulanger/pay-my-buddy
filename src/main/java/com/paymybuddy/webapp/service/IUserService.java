package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.User;

import java.util.Optional;

/**
 * Any class that handles user operations
 */
public interface IUserService {
    Optional<User> getUserByEmail(String email);
    Optional<User> getUserById(int userId);
    User saveUser(User user);

}
