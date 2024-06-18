package com.paymybuddy.webapp.security;

import com.paymybuddy.webapp.security.CustomOAuth2Service;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.service.IUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomOAuth2ServiceTest {
    @Mock
    private DefaultOAuth2UserService defaultOAuth2UserService;
    @Mock
    private IUserService userService;
    @Mock
    private OAuth2UserRequest oAuth2UserRequest;
    @Mock
    private OAuth2User oAuth2User;
    @Mock
    private ClientRegistration clientRegistration;
    @InjectMocks
    private CustomOAuth2Service service;

    @Test
    @DisplayName("Given that the email and name attributes exist, then create a user in database and return oauth user with email and name")
    public void loadUserTest() {
        //Expected user result
        User user = new User();
        user.setId(1);
        user.setUsername("Jane Doe");
        user.setEmail("jane_doe@mail.com");

        //Set attributes for oauthuser
        Map<String, Object> attributes = Map.of(
                "email", user.getEmail(),
                "name", user.getUsername());

        when(defaultOAuth2UserService.loadUser(any(OAuth2UserRequest.class))).thenReturn(oAuth2User);
        when(oAuth2User.getAttributes()).thenReturn(attributes);
        when(oAuth2UserRequest.getClientRegistration()).thenReturn(clientRegistration);
        when(clientRegistration.getRegistrationId()).thenReturn("github");
        when(userService.getUserByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userService.saveUser(any(User.class))).thenReturn(user);


        OAuth2User result = service.loadUser(oAuth2UserRequest);

        verify(oAuth2User, times(1)).getAttributes();
        verify(oAuth2UserRequest, times(1)).getClientRegistration();
        verify(userService, times(1)).getUserByEmail(user.getEmail());
        verify(userService, times(2)).saveUser(any(User.class));

        assertNotNull(result);
        assertEquals(user.getEmail(), result.getAttributes().get("email"));
        assertEquals(user.getUsername(), result.getAttributes().get("name"));
    }

    @Test
    @DisplayName("Given there's no email attribute, then create default email and save user in database and return oauth user with email and name")
    public void loadUser_whenNoEmailTest() {
        //Expected user result
        User user = new User();
        user.setId(1);
        user.setUsername("Jane Doe");
        user.setEmail("github_123@paymybuddy.com");

        //Set attributes for oauthuser
        Map<String, Object> attributes = Map.of(
                "email", user.getEmail(),
                "name", user.getUsername(),
                "id", "123");


        when(defaultOAuth2UserService.loadUser(any(OAuth2UserRequest.class))).thenReturn(oAuth2User);
        when(oAuth2User.getAttributes()).thenReturn(attributes);
        when(oAuth2UserRequest.getClientRegistration()).thenReturn(clientRegistration);
        when(clientRegistration.getRegistrationId()).thenReturn("github");
        when(userService.getUserByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userService.saveUser(any(User.class))).thenReturn(user);

        OAuth2User result = service.loadUser(oAuth2UserRequest);

        verify(oAuth2User, times(1)).getAttributes();
        verify(oAuth2UserRequest, times(1)).getClientRegistration();
        verify(userService, times(1)).getUserByEmail(user.getEmail());
        verify(userService, times(2)).saveUser(any(User.class));

        assertNotNull(result);
        assertEquals(user.getEmail(), result.getAttributes().get("email"));
        assertEquals(user.getUsername(), result.getAttributes().get("name"));
    }
}
