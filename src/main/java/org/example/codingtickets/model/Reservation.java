package org.example.codingtickets.model;

import org.example.codingtickets.exception.AnnulationTardiveException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Reservation {
    private Long id;
    private LocalDateTime dateReservation;
    private int nbPlaces;
    private BigDecimal montantTotal;
    private StatutReservation statut;
    private Client client;
    private Evenement evenement;

    public Reservation(Long id, LocalDateTime dateReservation, int nbPlaces,
                       BigDecimal montantTotal, StatutReservation statut,
                       Client client, Evenement evenement) {
        this.id = id;
        this.dateReservation = dateReservation;
        this.nbPlaces = nbPlaces;
        this.montantTotal = montantTotal;
        this.statut = statut;
        this.client = client;
        this.evenement = evenement;
    }
    
    /**
     * Constructeur par défaut.
     */
    public Reservation() {
    }

    /**
     * Annule la réservation si l'événement n'est pas trop proche.
     * @param maintenant L'instant actuel pour la vérification
     */
    public void annuler(LocalDateTime maintenant) {
        if (statut == StatutReservation.ANNULEE) {
            return;
        }

        LocalDateTime limite = maintenant.plusDays(1);
        if (evenement.getDateEvenement().isBefore(limite)) {
            throw new AnnulationTardiveException(
                "Annulation impossible : l'événement est trop proche (moins de 24h)."
            );
        }

        this.statut = StatutReservation.ANNULEE;
    }

    // ==================== GETTERS ====================
    
    public Long getId() { return id; }
    public LocalDateTime getDateReservation() { return dateReservation; }
    public int getNbPlaces() { return nbPlaces; }
    public BigDecimal getMontantTotal() { return montantTotal; }
    public StatutReservation getStatut() { return statut; }
    public Client getClient() { return client; }
    public Evenement getEvenement() { return evenement; }

    // ==================== SETTERS ====================
    
    public void setId(Long id) { this.id = id; }
    public void setDateReservation(LocalDateTime dateReservation) { this.dateReservation = dateReservation; }
    public void setNbPlaces(int nbPlaces) { this.nbPlaces = nbPlaces; }
    public void setMontantTotal(BigDecimal montantTotal) { this.montantTotal = montantTotal; }
    public void setStatut(StatutReservation statut) { this.statut = statut; }
    public void setClient(Client client) { this.client = client; }
    public void setEvenement(Evenement evenement) { this.evenement = evenement; }
}
