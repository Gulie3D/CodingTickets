package org.example.codingtickets.dao;

import org.example.codingtickets.model.Evenement;
import java.util.List;
import java.util.Optional;

public interface EvenementDAO {

    Evenement findById(long id);

    List<Evenement> findAll();

    Optional<Evenement> findById(Long id);

    void save(Evenement evenement);

    void delete(Long id);

    void update(Evenement evenement);

  }
