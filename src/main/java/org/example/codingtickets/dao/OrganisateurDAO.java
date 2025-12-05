package org.example.codingtickets.dao;

import org.example.codingtickets.model.Organisateur;

import java.util.List;

public interface OrganisateurDAO {

    Organisateur findById(Long id);

    List<Organisateur> findAll();

    Organisateur save(Organisateur organisateur);

    void delete(Long id);


}
