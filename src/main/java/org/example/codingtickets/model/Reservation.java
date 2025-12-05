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
            return; // déjà annulée, on ne fait rien
        }

        // Règle J-1 : on ne peut annuler que si la date de l'événement
        // est au moins dans 1 jour
        LocalDateTime limite = maintenant.plusDays(1);
        if (evenement.getDateEvenement().isBefore(limite)) {
            throw new AnnulationTardiveException(
                    "Annulation impossible : l'événement est trop proche."
            );
        }

        statut = StatutReservation.ANNULEE;
        evenement.annulerPlaces(nbPlaces); // R3.2
    }

    // Getters / setters
    public Long getId() { return id; }
    public StatutReservation getStatut() { return statut; }
    public Integer getNbPlaces() { return nbPlaces; }
    public BigDecimal getMontantTotal() { return montantTotal; }
    public Client getClient() { return client; }
    public Evenement getEvenement() { return evenement; }

    public LocalDateTime getDateReservation() {

        return dateReservation;
    }


}