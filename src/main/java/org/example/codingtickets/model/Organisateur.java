package org.example.codingtickets.model;

public class Organisateur extends Utilisateur {
    public Organisateur(Long id, String nom, String email, String motDePasse) {
        super(id, nom, email, motDePasse, Role.ORGANISATEUR);
    }

    public Organisateur(long idOrganisateur) {
        super();
    }
}
