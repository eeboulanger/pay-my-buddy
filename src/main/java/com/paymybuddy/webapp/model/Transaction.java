package com.paymybuddy.webapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull
    @Min(value = 1, message = "Minimal amount is 1")
    @Column(name = "amount", nullable = false)
    private double amount;

    @NotEmpty
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "date", nullable = false)
    private Timestamp date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender", referencedColumnName = "id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver", referencedColumnName = "id")
    private User receiver;

    public Transaction() {
    }

    public Transaction(double amount, String description, Timestamp date, User sender, User receiver) {
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.sender = sender;
        this.receiver = receiver;
    }
}
