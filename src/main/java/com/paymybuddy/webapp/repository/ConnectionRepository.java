package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.Connection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionRepository extends CrudRepository<Connection, Long> {
}
