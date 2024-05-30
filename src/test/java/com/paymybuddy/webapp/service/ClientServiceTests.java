package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.Client;
import com.paymybuddy.webapp.repository.ClientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTests {
    @Mock
    private ClientRepository repository;
    @InjectMocks
    private ClientService service;

    @Test
    @DisplayName("Given there's a client with the email, then return the client")
    public void getClientByEmailTest() {
        Client client = new Client();
        String email = "john_doe@mail.com";
        client.setEmail(email);
        when(repository.findByEmail(email)).thenReturn(Optional.of(client));

        Optional<Client> result = service.getClientByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
    }

    @Test
    @DisplayName("Given there's no client with the email, then return empty")
    public void givenThereIsNoEmail_whenGetByEmail_thenReturnEmpty() {
        when(repository.findByEmail("john_doe@mail.com")).thenReturn(Optional.empty());

        Optional<Client> result = service.getClientByEmail("john_doe@mail.com");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Create client should save client in database and return client as an object")
    public void createClientTest() {
        Client client = new Client();
        Client saved = new Client();
        when(repository.save(client)).thenReturn(saved);

        Client result = service.createClient(client);

        assertNotNull(result);
        assertEquals(saved, result);
    }
}
