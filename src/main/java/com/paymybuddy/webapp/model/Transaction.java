package com.paymybuddy.webapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Value;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull
    @Min(value = 1, message = "Montant minimum est 1")
    @Column(name = "amount")
    private double amount;

    @NotNull
    @Size(min = 1, max = 100)
    @NotEmpty(message = "Veuillez ajouter une description")
    @NotBlank
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "transaction_date")
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
