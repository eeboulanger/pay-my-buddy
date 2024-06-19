package com.paymybuddy.webapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.paymybuddy.webapp.exception.PaymentException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull
    @Column(name = "balance")
    private double balance;

    @OneToOne
    @JsonBackReference
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public void debit(double amount) throws PaymentException {
        if (balance < amount) {
            throw new PaymentException("Les fonds sont insuffisants");
        } else {
            balance -= amount;
        }
    }

    public void credit(double amount) {
        balance += amount;
    }
}
