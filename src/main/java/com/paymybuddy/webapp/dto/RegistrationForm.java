package com.paymybuddy.webapp.dto;

import lombok.Data;
import lombok.ToString;

@Data
public class RegistrationForm {
    private String email;
    @ToString.Exclude
    private String password;
    private String username;

    public RegistrationForm() {
    }

    public RegistrationForm(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }
}
