package org.example.codingtickets.model;

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

    public void annuler() {
        if (statut == StatutReservation.ANNULEE) return;
        statut = StatutReservation.ANNULEE;
        evenement.annulerPlaces(nbPlaces);
    }

    // Getters / setters
    public Long getId() { return id; }
    public StatutReservation getStatut() { return statut; }
    public Client getClient() { return client; }
    public Evenement getEvenement() { return evenement; }
}