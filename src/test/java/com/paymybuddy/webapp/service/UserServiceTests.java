package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository repository;
    @InjectMocks
    private UserService service;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setEmail("JoHn@mail.com");
    }

    @Test
    @DisplayName("Given there's a user with the email, then return the user")
    public void getUserByEmailTest() {
        String email = "john_doe@mail.com";
        user.setEmail(email);
        when(repository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> result = service.getUserByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
    }

    @Test
    @DisplayName("Given there's no user with the email, then return empty")
    public void givenThereIsNoEmail_whenGetByEmail_thenReturnEmpty() {
        when(repository.findByEmail("john_doe@mail.com")).thenReturn(Optional.empty());

        Optional<User> result = service.getUserByEmail("john_doe@mail.com");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Create user should save user in database with email in lower case and return user as an object")
    public void createClientTest() {
        when(repository.save(user)).thenReturn(user);

        User result = service.saveUser(user);

        assertNotNull(result);
        assertEquals(user.getEmail().toLowerCase(), result.getEmail());
    }

    @Test
    @DisplayName("Given there's a user with the id, then return user")
    public void getUserByIdTest() {
        user.setId(1);
        when(repository.findById(1)).thenReturn(Optional.of(user));

        Optional<User> result = service.getUserById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    @DisplayName("Given there's no user with the id, then return empty")
    public void givenThereIsNoUserWithId_whenGetById_thenReturnEmpty() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        Optional<User> result = service.getUserById(1);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Given there's a user when delete user, then delete user from database")
    public void deleteUserSuccessTest() {
        assertDoesNotThrow(() -> service.deleteById(1));
    }

    @Test
    public void getAllUsersTest() {
        Iterable<User> iterable = mock(Iterable.class);

        when(repository.findAll()).thenReturn(iterable);

        Iterable<User> result = service.getAllUsers();
        assertEquals(result, iterable);
    }
}
