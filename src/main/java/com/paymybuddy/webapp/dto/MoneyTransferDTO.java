package com.paymybuddy.webapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MoneyTransferDTO {
    @NotNull
    private int receiverId;
    @NotNull
    @Min(value = 1, message = "Minimal amount is 1")
    private double amount;
    @NotEmpty
    private String description;
}
