package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.exception.UserNotFoundException;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.security.IAuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserConnectionServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private IAuthenticationService authenticationService;
    @InjectMocks
    private UserConnectionService service;
    private User user;
    private User connection;
    private String email;

    @BeforeEach
    public void setUp() {
        connection = new User();
        connection.setUsername("Jane");
        user = new User();
        email = "jane_doe@mail.com";
        user.setEmail(email);

        user.setConnections(Set.of(connection));
    }

    @Test
    @DisplayName("Given user exists in database, when add user connection, then create new user connection and add to authenticated user")
    public void addNewUserConnection() throws UserNotFoundException {
        User authUser = new User();

        when(userService.getUserByEmail(email)).thenReturn(Optional.of(user));
        when(authenticationService.getCurrentUser()).thenReturn(authUser);

        service.addUserConnection(email);

        verify(userService).getUserByEmail(email);
        verify(authenticationService, times(1)).getCurrentUser();
        verify(userService, times(1)).saveUser(any(User.class));
    }

    @Test
    @DisplayName("Given the email is the same as current user, when add user connection, then throw exception")
    public void addNewUserConnection_whenSameEmail_shouldFail() {
        User authUser = new User();
        authUser.setEmail(email);

        when(userService.getUserByEmail(email)).thenReturn(Optional.of(user));
        when(authenticationService.getCurrentUser()).thenReturn(authUser);

        assertThrows(UserNotFoundException.class, () -> service.addUserConnection(email));

        verify(userService).getUserByEmail(email);
        verify(authenticationService, times(1)).getCurrentUser();
        verify(userService, never()).saveUser(any(User.class));
    }

    @Test
    @DisplayName("No user in database with the given email should throw exception")
    public void givenThereIsNoUserWithEmail_whenAddNewUserConnection_thenThrowException() {
        when(userService.getUserByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.addUserConnection(email));

        verify(userService).getUserByEmail(email);
        verify(authenticationService, never()).getCurrentUser();
        verify(userService, never()).saveUser(any(User.class));
    }

    @Test
    @DisplayName("No user in database with the authentication email should fail and throw exception")
    public void givenThereIsNoAuthUserInDataBase_whenAddNewUserConnection_thenThrowException() {
        when(userService.getUserByEmail(email)).thenReturn(Optional.of(user));
        when(authenticationService.getCurrentUser()).thenThrow(UsernameNotFoundException.class);

        assertThrows(UsernameNotFoundException.class, () -> service.addUserConnection(email));

        verify(userService).getUserByEmail(email);
        verify(authenticationService).getCurrentUser();
        verify(userService, never()).saveUser(any(User.class));
    }
}
