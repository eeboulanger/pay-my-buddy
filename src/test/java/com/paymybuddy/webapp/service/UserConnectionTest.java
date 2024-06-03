package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserConnectionTest {
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationFacade authenticationFacade;
    @InjectMocks
    private UserConnectionService service;
    @Mock
    private Authentication auth;
    private User user;
    private String email;

    @BeforeEach
    public void setUp() {
        user = new User();
        email = "jane_doe@mail.com";
        user.setEmail(email);
    }

    @Test
    @DisplayName("Given user exists in database, when add user connection, then create new user connection and add to authenticated user")
    public void addNewUserConnection() {
        User authUser = new User();

        when(userService.getUserByEmail(email)).thenReturn(Optional.of(user));
        when(authenticationFacade.getAuthentication()).thenReturn(auth);
        when(userService.getUserByEmail(auth.getName())).thenReturn(Optional.of(authUser));

        service.addUserConnection(email);

        verify(userService).getUserByEmail(email);
        verify(authenticationFacade, times(1)).getAuthentication();
        verify(userService).getUserByEmail(auth.getName());
    }

    @Test
    @DisplayName("No user in database with the given email should throw exception")
    public void givenThereIsNoUserWithEmail_whenAddNewUserConnection_thenThrowException() {
        when(userService.getUserByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> service.addUserConnection(email));

        verify(userService).getUserByEmail(email);
        verify(authenticationFacade, never()).getAuthentication();
        verify(userService, never()).getUserByEmail(auth.getName());
    }

    @Test
    @DisplayName("No user in database with the authentication email should fail and throw exception")
    public void givenThereIsNoAuthUserInDataBase_whenAddNewUserConnection_thenThrowException() {
        when(userService.getUserByEmail(email)).thenReturn(Optional.of(user));
        when(authenticationFacade.getAuthentication()).thenReturn(auth);
        when(userService.getUserByEmail(auth.getName())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> service.addUserConnection(email));

        verify(userService).getUserByEmail(email);
        verify(authenticationFacade).getAuthentication();
        verify(userService).getUserByEmail(auth.getName());
    }
}
