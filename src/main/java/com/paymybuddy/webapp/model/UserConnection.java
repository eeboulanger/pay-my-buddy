package com.paymybuddy.webapp.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_connections")
public class UserConnection {

    @EmbeddedId
    private UserConnectionId id;
}
