package com.paymybuddy.webapp.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class ConnectionId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    private Client clientId;

    @ManyToOne
    @JoinColumn(name = "connection_id", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    private Client connectionId;

    public ConnectionId(Client clientId, Client connectionId) {
        this.clientId = clientId;
        this.connectionId = connectionId;
    }
}
