package com.paymybuddy.webapp.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
public class ConnectionIdTest {

    @Mock
    private Client client;
    @Mock
    private Client connection;
    private ConnectionId connectionId;

    @Test
    public void testGettersAndSetters() {
        connectionId = new ConnectionId(client, connection);

        assertEquals(client, connectionId.getClientId());
        assertEquals(connection, connectionId.getConnectionId());
    }

    @Test
    public void testEqualsAndHashCode() {
        connectionId = new ConnectionId(client, connection);
        ConnectionId connectionId2 = new ConnectionId(client, connection);

        assertEquals(connectionId, connectionId2);
        assertEquals(connectionId.hashCode(), connectionId2.hashCode());

        connectionId.setClientId(new Client());
        assertNotEquals(connectionId, connectionId2);
        assertNotEquals(connectionId.hashCode(), connectionId2.hashCode());
    }
}
