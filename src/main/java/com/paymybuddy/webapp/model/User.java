package com.paymybuddy.webapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    @NotBlank
    @NotNull
    @NotEmpty
    @Size(min = 1, max = 50)
    private String username;

    @Email
    @NotBlank
    @NotEmpty
    @NotNull
    @Column(name = "email", unique = true)
    private String email;

    //Keep nullable for oauth2 user accounts
    @ToString.Exclude
    @Column(name = "password")
    private String password;

    @NotNull
    @NotBlank
    @NotEmpty
    @Column(name = "role")
    private String role;

    @JsonManagedReference
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Account account;

    @JsonBackReference
    @ManyToMany(
            fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_connections",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "connected_user_id")
    )
    private List<User> connections = new ArrayList<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(username, user.username) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password);
    }

    public int hashCode() {
        return Objects.hash(id, username, email, password);
    }
}
