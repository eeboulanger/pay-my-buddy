package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    public void findByEmailTest() {
        String email = "jane_doe@mail.com";
        BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

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
}
