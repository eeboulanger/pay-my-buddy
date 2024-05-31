package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends CrudRepository<User, Integer> {

    Optional<User> findByEmail(String email);
}
