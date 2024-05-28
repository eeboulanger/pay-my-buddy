package com.paymybuddy.webapp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "connection")
public class Connection {

    @EmbeddedId
    private ConnectionId connectionId;

    @Column(name = "connection_name")
    private String connectionName;

    @OneToMany
            (fetch = FetchType.LAZY
    )
    @JoinColumns({
            @JoinColumn(name = "client_id", referencedColumnName = "client_id", insertable = false, updatable = false),
            @JoinColumn(name = "connection_id", referencedColumnName = "connection_id", insertable = false, updatable = false)
    })
    private List<Transaction> transactions = new ArrayList<>();
}
