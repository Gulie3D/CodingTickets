package org.example.codingtickets.model;

public class Client extends Utilisateur {
    public Client(Long id, String nom, String email, String motDePasse) {
        super(id, nom, email, motDePasse, Role.CLIENT);
    }
}
