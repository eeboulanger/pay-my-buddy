package com.paymybuddy.webapp.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.ToString;

@Data
public class RegistrationForm {
    @Email
    @NotBlank
    private String email;

    @ToString.Exclude
    @NotBlank
    @Size(min=8, max = 50, message = "Password must be between 8 and 30 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$",
            message = "Password most contain at least one upper case letter and one special character")
    private String password;

    @NotBlank
    @Size(min=1, max=50)
    private String username;

    public RegistrationForm() {
    }

    public RegistrationForm(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }
}
