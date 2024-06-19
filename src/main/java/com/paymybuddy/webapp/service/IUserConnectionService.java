package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.exception.UserNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Any class that handles adding user connections to a user
 */
public interface IUserConnectionService {

    void addUserConnection(String email) throws UsernameNotFoundException, UserNotFoundException;
}
