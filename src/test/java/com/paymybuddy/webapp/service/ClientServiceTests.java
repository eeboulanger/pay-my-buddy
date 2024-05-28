package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTests {
    @Mock
    private ClientRepository repository;
    @InjectMocks
    private ClientService service;

    @Test
    public void getClientTest(){

    }

    @Test
    public void createClientTest(){

    }


}
