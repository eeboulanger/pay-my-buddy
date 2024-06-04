package com.paymybuddy.webapp.dto;

import com.paymybuddy.webapp.model.User;
import lombok.Data;

@Data
public class MoneyTransferDTO {

    private int receiverId;
    private double amount;
    private String description;
}
