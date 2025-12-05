package org.example.codingtickets.model;

public class Utilisateur {
    private Long id;
    private String nom;
    private String email;
    private String motDePasse;
    private Role role;

    public Utilisateur(Long id, String nom, String email, String motDePasse, Role role) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    public Utilisateur() {

    }


    // Getters / setters
    public Long getId() { return id; }
    public String getNom() { return nom; }
    public String getEmail() { return email; }
    public String getMotDePasse() { return motDePasse; }
    public Role getRole() { return role; }

    public void setId(Long id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setEmail(String email) { this.email = email; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    public void setRole(Role role) { this.role = role; }
}

