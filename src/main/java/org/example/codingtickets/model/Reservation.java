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

    public void annuler(LocalDateTime maintenant) {
        if (statut == StatutReservation.ANNULEE) {
            return;
        }

        LocalDateTime limite = maintenant.plusDays(1);
        if (evenement.getDateEvenement().isBefore(limite)) {
            throw new AnnulationTardiveException("Annulation impossible : l'événement est trop proche (moins de 24h).");
        }

        this.statut = StatutReservation.ANNULEE;
    }

    // Getter / setter
    public Long getId() { return id; }
    public StatutReservation getStatut() { return statut; }
    public Integer getNbPlaces() { return nbPlaces; }
    public BigDecimal getMontantTotal() { return montantTotal; }
    public Client getClient() { return client; }
    public Evenement getEvenement() { return evenement; }

    public Long setId(Long id) { return this.id = id;}

    public LocalDateTime getDateReservation() {

        return dateReservation;
    }
}