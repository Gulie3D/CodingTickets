package org.example.codingtickets.dao;

import org.example.codingtickets.model.Organisateur;
import java.util.List;
import java.util.Optional;

public interface OrganisateurDAO {

    Organisateur findById(long id);

    List<Organisateur> findAll();

    Optional<Organisateur> findById(Long id);

    Organisateur save(Organisateur organisateur);

    void delete(Long id);


}
