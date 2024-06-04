package com.paymybuddy.webapp.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserGettersAndSetters() {
        User user = new User();
        user.setId(1);
        user.setUsername("John");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");

        assertEquals(1L, user.getId());
        assertEquals("John", user.getUsername());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
    }

    @Test
    public void testToStringExcludesPassword() {
        User user = new User();
        user.setId(1);
        user.setUsername("John");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");

        String userString = user.toString();
        assertFalse(userString.contains("password123"));
    }

    @Test
    public void testUserEqualsAndHashCode() {
        User user1 = new User();
        user1.setId(1);
        user1.setUsername("John");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("password123");

        User user2 = new User();
        user2.setId(1);
        user2.setUsername("John");
        user2.setEmail("john.doe@example.com");
        user2.setPassword("password123");

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());

        user2.setId(2);
        assertNotEquals(user1, user2);
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }
}
