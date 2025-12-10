package org.example.codingtickets.dao;

import org.example.codingtickets.model.Client;
import org.example.codingtickets.model.Utilisateur;

import java.util.List;
import java.util.Optional;

public interface UtilisateurDAO {

    Utilisateur findById(long id);

    List<Utilisateur> findAll();

    Optional<Utilisateur> findById(Long id);

    void save(Utilisateur utilisateur);

    void delete(Long id);

    Utilisateur findByEmailAndPassword(String email, String password);

    Utilisateur findByEmail(String email);

    /**
     * Récupère tous les utilisateurs ayant le rôle CLIENT.
     * @return Liste des clients
     */
    List<Client> findAllClients();
}
