package com.paymybuddy.webapp.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Handles loading user details
 */
public interface ICustomUserDetailsService extends UserDetailsService {
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
}
