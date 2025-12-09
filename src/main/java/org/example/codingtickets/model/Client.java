package org.example.codingtickets.model;

public class Client extends Utilisateur {
    
    public Client(Long id, String nom, String email, String motDePasse) {
        super(id, nom, email, motDePasse, Role.CLIENT);
    }

    /**
     * Constructeur avec uniquement l'ID (pour les références).
     * Initialise l'utilisateur avec le rôle CLIENT.
     */
    public Client(Long id) {
        super();
        this.setId(id);
        this.setRole(Role.CLIENT);
    }
    
    /**
     * Constructeur par défaut.
     */
    public Client() {
        super();
        this.setRole(Role.CLIENT);
    }
}
