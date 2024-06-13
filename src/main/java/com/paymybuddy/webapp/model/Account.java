package com.paymybuddy.webapp.model;

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

    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public void debit(double amount) throws PaymentException {
        if (balance < amount) {
            throw new PaymentException("Funds are insufficient");
        } else {
            balance -= amount;
        }
    }

    public void credit(double amount) {
        balance += amount;
    }
}
