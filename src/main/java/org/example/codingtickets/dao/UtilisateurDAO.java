package org.example.codingtickets.dao;

import org.example.codingtickets.model.Utilisateur;

import java.util.List;
import java.util.Optional;

public interface UtilisateurDAO {

    Utilisateur findById(long id);

    List<Utilisateur> findAll();

    Optional<Utilisateur> findById(Long id);

    Utilisateur save(Utilisateur utilisateur);

    void delete(Long id);

    Utilisateur findByEmailAndPassword(String email, String password);

}
