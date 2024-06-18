package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    @DisplayName("Given that the user exists, when find user by email, then return user")
    public void findByEmailTest() {
        String email = "jane_doe@mail.com";

        Optional<User> result = repository.findByEmail(email);

        assertTrue(result.isPresent());

        User user = result.get();

        //check user info
        assertEquals("Jane", user.getUsername());
        assertEquals(2, user.getId());
        assertEquals("jane_doe@mail.com", user.getEmail());
        assertTrue(passwordEncoder.matches("1234@Abcd", user.getPassword()));

        //check user's account info
        assertEquals(2, user.getAccount().getUser().getId());
        assertEquals(100, user.getAccount().getBalance());

        //check user's connections info
        assertEquals(2, user.getConnections().size());
        assertTrue(user.getConnections().stream().anyMatch(u -> u.getUsername().equals("Jimmie")));
        assertTrue(user.getConnections().stream().anyMatch(u -> u.getUsername().equals("John")));
    }

    @Test
    @DisplayName("Given that the user doesn't exists, when find user by email, then return empty optional")
    public void findByEmail_whenUserDoesntExistTest() {
        String email = "noSuchUser";

        Optional<User> result = repository.findByEmail(email);

        assertTrue(result.isEmpty());
    }

    @Test
    public void saveUserTest() {
        User user = repository.findByEmail("jane_doe@mail.com").orElse(new User());
        user.setUsername("New username");

        User result = repository.save(user);

        assertNotNull(result);
        assertEquals("New username", result.getUsername());
    }

    @Test
    public void getUserByIdTest() {
        Optional<User> result = repository.findById(1);

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getUsername());
    }

    @Test
    public void deleteUserByIdTest() {
        repository.deleteById(1);

        Optional<User> result = repository.findById(1);
        assertTrue(result.isEmpty());
    }
}
