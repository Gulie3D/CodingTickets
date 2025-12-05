package org.example.codingtickets.dao;

import org.example.codingtickets.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientDAO {

    Client findById(long id);

    List<Client> findAll();

    Optional<Client> findById(Long id);

    Client save(Client client);

    void delete(Long id);

}
