package com.paymybuddy.webapp.security;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * Handles OAuth2Service user details
 */
public interface ICustomOAuth2Service extends OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException;
}
