package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.repository.ConnectionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ConnectionServiceTest {
    @Mock
    private ConnectionRepository repository;
    @InjectMocks
    private ConnectionService service;

    @Test
    public void addNewConnectionTest(){
    }
    @Test
    public void getConnectionsTest(){

    }

}
