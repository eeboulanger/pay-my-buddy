package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.Client;
import com.paymybuddy.webapp.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    public Optional<Client> getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    public Client createClient(Client client) {
        return clientRepository.save(client);
    }
}
