package org.example.codingtickets.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Evenement {
    private Long id;
    private String titre;
    private String description;
    private LocalDateTime dateEvenement;
    private String lieu;
    private int nbPlacesTotales;
    private int nbPlacesRestantes;
    private BigDecimal prixBase;
    private Organisateur organisateur;

    public Evenement(Long id, String titre, String description,
                     LocalDateTime dateEvenement, String lieu,
                     int nbPlacesTotales, int nbPlacesRestantes,
                     BigDecimal prixBase, Organisateur organisateur) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.dateEvenement = dateEvenement;
        this.lieu = lieu;
        this.nbPlacesTotales = nbPlacesTotales;
        this.nbPlacesRestantes = nbPlacesRestantes;
        this.prixBase = prixBase;
        this.organisateur = organisateur;
    }

    // Règle métier simple
    public void reserverPlaces(int nb) {
        if (nb <= 0) {
            throw new IllegalArgumentException("Nombre de places doit être > 0");
        }
        if (nb > nbPlacesRestantes) {
            throw new IllegalStateException("Pas assez de places restantes");
        }
        nbPlacesRestantes -= nb;
    }

    public void annulerPlaces(int nb) {
        nbPlacesRestantes += nb;
        if (nbPlacesRestantes > nbPlacesTotales) {
            nbPlacesRestantes = nbPlacesTotales;
        }
    }

    // Getters / setters classiques…

    public Long getId() { return id; }
    public String getTitre() { return titre; }
    public String getDescription() { return description; }
    public LocalDateTime getDateEvenement() { return dateEvenement; }
    public String getLieu() { return lieu; }
    public int getNbPlacesTotales() { return nbPlacesTotales; }
    public int getNbPlacesRestantes() { return nbPlacesRestantes; }
    public BigDecimal getPrixBase() { return prixBase; }
    public Organisateur getOrganisateur() { return organisateur; }

    public void setId(Long id) { this.id = id; }
    // … (ajoute les autres setters si tu veux)
}
