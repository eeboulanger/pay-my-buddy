package com.paymybuddy.webapp.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {

    @Test
    public void testUserGettersAndSetters() {
        Client client = new Client();
        client.setId(1L);
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setEmail("john.doe@example.com");
        client.setPassword("password123");

        assertEquals(1L, client.getId());
        assertEquals("John", client.getFirstName());
        assertEquals("Doe", client.getLastName());
        assertEquals("john.doe@example.com", client.getEmail());
        assertEquals("password123", client.getPassword());
    }

    @Test
    public void testToStringExcludesPassword() {
        Client client = new Client();
        client.setId(1L);
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setEmail("john.doe@example.com");
        client.setPassword("password123");

        String userString = client.toString();
        assertFalse(userString.contains("password123"));
    }

    @Test
    public void testClientEqualsAndHashCode() {
        Client client1 = new Client();
        client1.setId(1L);
        client1.setFirstName("John");
        client1.setLastName("Doe");
        client1.setEmail("john.doe@example.com");
        client1.setPassword("password123");

        Client client2 = new Client();
        client2.setId(1L);
        client2.setFirstName("John");
        client2.setLastName("Doe");
        client2.setEmail("john.doe@example.com");
        client2.setPassword("password123");

        assertEquals(client1, client2);
        assertEquals(client1.hashCode(), client2.hashCode());

        client2.setId(2L);
        assertNotEquals(client1, client2);
        assertNotEquals(client1.hashCode(), client2.hashCode());
    }
}
