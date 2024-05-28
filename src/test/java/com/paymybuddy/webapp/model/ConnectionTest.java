package com.paymybuddy.webapp.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
public class ConnectionTest {
    @Mock
    private Transaction transaction;
    @Mock
    private ConnectionId connectionId;

    @Test
    public void testGettersAndSetters() {
        Connection connection = new Connection();
        connection.setConnectionId(connectionId);
        connection.setConnectionName("John Doe");
        connection.setTransactions(List.of(transaction));

        assertEquals("John Doe", connection.getConnectionName());
        assertEquals(connectionId, connection.getConnectionId());
        assertEquals(transaction, connection.getTransactions().get(0));
    }

    @Test
    public void testBeneficiaryEqualsAndHashCode() {
        Connection connection1 = new Connection();
        connection1.setConnectionId(connectionId);
        connection1.setConnectionName("John Doe");
        connection1.setTransactions(List.of(transaction));

        Connection connection2 = new Connection();
        connection2.setConnectionId(connectionId);
        connection2.setConnectionName("John Doe");
        connection2.setTransactions(List.of(transaction));

        assertEquals(connection1, connection2);
        assertEquals(connection1.hashCode(), connection2.hashCode());

        connection1.setConnectionName("Marie Doe");

        assertNotEquals(connection1, connection2);
        assertNotEquals(connection1.hashCode(), connection2.hashCode());
    }
}
