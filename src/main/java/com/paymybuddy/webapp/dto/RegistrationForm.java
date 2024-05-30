package com.paymybuddy.webapp.dto;

import lombok.Data;
import lombok.ToString;

@Data
public class RegistrationForm {
    private String email;
    @ToString.Exclude
    private String password;
    private String firstName;
    private String lastName;

    public RegistrationForm() {
    }

    public RegistrationForm(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
