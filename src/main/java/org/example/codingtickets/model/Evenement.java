package org.example.codingtickets.model;

import org.example.codingtickets.exception.PlacesInsuffisantesException;

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


    /**
     * Réserve un certain nombre de places.
     * Met à jour nbPlacesRestantes ou lève une exception.
     */
    public void reserverPlaces(int nb) {
        if (nb <= 0) {
            throw new IllegalArgumentException("Le nombre de places doit être > 0");
        }
        if (nb > nbPlacesRestantes) {
            throw new PlacesInsuffisantesException(
                    "Il ne reste que " + nbPlacesRestantes + " place(s) pour cet événement."
            );
        }
        nbPlacesRestantes -= nb;
    }

    /**
     * Annule un certain nombre de places : remet les places dans le stock.
     */
    public void annulerPlaces(int nb) {
        nbPlacesRestantes += nb;
        if (nbPlacesRestantes > nbPlacesTotales) {
            nbPlacesRestantes = nbPlacesTotales;
        }
    }

    // ==================== GETTERS ====================

    public Long getId() { return id; }
    public String getTitre() { return titre; }
    public String getDescription() { return description; }
    public LocalDateTime getDateEvenement() { return dateEvenement; }
    public String getLieu() { return lieu; }
    public int getNbPlacesTotales() { return nbPlacesTotales; }
    public int getNbPlacesRestantes() { return nbPlacesRestantes; }
    public BigDecimal getPrixBase() { return prixBase; }
    public Organisateur getOrganisateur() { return organisateur; }

    // ==================== SETTERS ====================

    public void setId(Long id) { this.id = id; }
    public void setTitre(String titre) { this.titre = titre; }
    public void setDescription(String description) { this.description = description; }
    public void setDateEvenement(LocalDateTime dateEvenement) { this.dateEvenement = dateEvenement; }
    public void setLieu(String lieu) { this.lieu = lieu; }
    public void setNbPlacesTotales(int nbPlacesTotales) { this.nbPlacesTotales = nbPlacesTotales; }
    public void setNbPlacesRestantes(int nbPlacesRestantes) { this.nbPlacesRestantes = nbPlacesRestantes; }
    public void setPrixBase(BigDecimal prixBase) { this.prixBase = prixBase; }
    public void setOrganisateur(Organisateur organisateur) { this.organisateur = organisateur; }
}
