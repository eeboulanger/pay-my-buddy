package com.paymybuddy.webapp.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Any class that handles adding user connections to a user
 */
public interface IUserConnectionService {

    void addUserConnection(String email) throws UsernameNotFoundException;
}
