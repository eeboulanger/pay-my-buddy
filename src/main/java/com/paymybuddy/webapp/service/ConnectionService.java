package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.repository.ConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConnectionService {
    @Autowired
    private ConnectionRepository connectionRepository;
}
