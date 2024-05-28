package com.paymybuddy.webapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @ToString.Exclude
    @Column(name = "password", nullable = false)
    private String password;

    @OneToOne(mappedBy = "client")
    private Account account;

    @OneToMany
            (cascade = CascadeType.ALL,
                    orphanRemoval = true,
                    fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private List<Connection> connections = new ArrayList<>();
}
