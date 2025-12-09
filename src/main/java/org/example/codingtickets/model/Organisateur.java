package org.example.codingtickets.model;

public class Organisateur extends Utilisateur {
    
    public Organisateur(Long id, String nom, String email, String motDePasse) {
        super(id, nom, email, motDePasse, Role.ORGANISATEUR);
    }

    /**
     * Constructeur avec uniquement l'ID (pour les références).
     * Initialise l'utilisateur avec le rôle ORGANISATEUR.
     */
    public Organisateur(Long id) {
        super();
        this.setId(id);
        this.setRole(Role.ORGANISATEUR);
    }
    
    /**
     * Constructeur par défaut.
     */
    public Organisateur() {
        super();
        this.setRole(Role.ORGANISATEUR);
    }
}
