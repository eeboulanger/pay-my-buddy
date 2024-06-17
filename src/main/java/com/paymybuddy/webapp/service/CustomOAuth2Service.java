package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.Account;
import com.paymybuddy.webapp.model.User;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Fetches oauth name and email attributes from provider and saves as new user in database if first connexion.
 */
@Service
public class CustomOAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final Logger logger = LoggerFactory.getLogger(CustomOAuth2Service.class);
    private final IUserService userService;
    private final DefaultOAuth2UserService delegate;

    @Autowired
    public CustomOAuth2Service(IUserService userService, DefaultOAuth2UserService delegate) {
        this.userService = userService;
        this.delegate = delegate;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        //Get email and username
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        logger.info("Retrieving attributes for user = " + name + "from provider = " + provider);

        if (email == null) {
            email = provider + "_" + attributes.get("id") + "@paymybuddy.com";
        }

        Optional<User> optionalUser = userService.getUserByEmail(email);

        if (optionalUser.isPresent()) {
            logger.info("User exists, logging in");
        } else {
            logger.info("User doesn't exist: " + email + " creating new user in database");
            saveUserAndAccount(email, name);
        }

        //Only store email and username attributes in database
        Map<String, Object> filteredAttributes = new HashMap<>();
        filteredAttributes.put("email", email);
        filteredAttributes.put("name", name);

        return new DefaultOAuth2User(
                Collections.singleton(new OAuth2UserAuthority(filteredAttributes)),
                filteredAttributes,
                "email"
        );
    }

    /**
     * Saves a new user with a user account to the database
     *
     * @param email and name of the user
     */

    @Transactional
    private void saveUserAndAccount(String email, String name) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(name);
        user.setRole("USER");
        user = userService.saveUser(user);

        Account account = new Account();
        account.setBalance(0.00);
        account.setUser(user);
        user.setAccount(account);
        userService.saveUser(user);
    }
}

