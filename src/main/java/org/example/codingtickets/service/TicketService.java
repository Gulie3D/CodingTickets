package org.example.codingtickets.service;

import org.example.codingtickets.dao.EvenementDAO;
import org.example.codingtickets.dao.ReservationDAO;
import org.example.codingtickets.dao.UtilisateurDAO;
import org.example.codingtickets.dao.jdbc.JdbcEvenementDAO;
import org.example.codingtickets.dao.jdbc.JdbcReservationDAO;
import org.example.codingtickets.dao.jdbc.JdbcUtilisateurDAO;
import org.example.codingtickets.model.*;
import org.example.codingtickets.utils.PasswordUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TicketService {
    private final UtilisateurDAO utilisateurDao;
    private final EvenementDAO evenementDao;
    private final ReservationDAO reservationDao;

    public TicketService() {
        this.utilisateurDao = new JdbcUtilisateurDAO();
        this.evenementDao = new JdbcEvenementDAO();
        this.reservationDao = new JdbcReservationDAO();
    }

    /**
     * Authentification via la BDD
     */
    public Utilisateur authentifier(String email, String motDePasse) {
        Utilisateur utilisateur = utilisateurDao.findByEmail(email);

        if (utilisateur != null &&
                PasswordUtils.checkPassword(motDePasse, utilisateur.getMotDePasse())) {
            return utilisateur;
        }

        return null;
    }

    /**
     * Liste tous les événements depuis la BDD
     */
    public List<Evenement> listerEvenements() {
        return evenementDao.findAll();
    }

    /**
     * Trouve un événement par ID
     */
    public Evenement trouverEvenementParId(long id) {
        return evenementDao.findById(id);
    }

    /**
     * Effectue une réservation
     */
    public void reserver(Client client, long idEvenement, int nbPlaces) {
        Evenement evenement = evenementDao.findById(idEvenement);

        if (evenement == null) {
            throw new IllegalArgumentException("Événement introuvable");
        }

        evenement.reserverPlaces(nbPlaces);
        evenementDao.update(evenement);

        BigDecimal montantTotal = evenement.getPrixBase().multiply(BigDecimal.valueOf(nbPlaces));

        Reservation reservation = new Reservation(
                0L,
                LocalDateTime.now(),
                nbPlaces,
                montantTotal,
                StatutReservation.CONFIRMEE,
                client,
                evenement
        );

        reservationDao.save(reservation);
    }

    /**
     * Liste les réservations d'un client spécifique via la BDD
     */
    public List<Reservation> listerReservationsClient(Client client) {
        return reservationDao.findByClient(client.getId());
    }

    /**
     * Annule une réservation
     */
    public void annulerReservation(long idReservation, Client client) {
        Reservation r = reservationDao.findById(idReservation);

        if (r == null || !r.getClient().getId().equals(client.getId())) {
            throw new IllegalArgumentException("Réservation introuvable ou non liée à ce client");
        }

        r.annuler(LocalDateTime.now());
        reservationDao.update(r);

        Evenement evt = r.getEvenement();
        evt.annulerPlaces(r.getNbPlaces());
        evenementDao.update(evt);
    }

    /**
     * Création d'événement pour les organisateurs
     */
    public void creerEvenement(Organisateur org, String titre, String description,
                               LocalDateTime date, String lieu, int nbPlaces, BigDecimal prixBase) {
        Evenement ev = new Evenement(
                null,
                titre,
                description,
                date,
                lieu,
                nbPlaces,
                nbPlaces,
                prixBase,
                org
        );
        evenementDao.save(ev);
    }

    /**
     * Modification d'un événement par son organisateur
     */
    public void modifierEvenement(Organisateur org, long idEvenement, String titre, String description,
                                  LocalDateTime date, String lieu, int nbPlaces, BigDecimal prixBase) {
        Evenement ev = evenementDao.findById(idEvenement);
        
        if (ev == null) {
            throw new IllegalArgumentException("Événement introuvable");
        }
        
        // Vérifier que l'organisateur est bien le propriétaire
        if (!ev.getOrganisateur().getId().equals(org.getId())) {
            throw new IllegalArgumentException("Vous n'êtes pas autorisé à modifier cet événement");
        }
        
        // Calculer les places déjà réservées
        int placesReservees = ev.getNbPlacesTotales() - ev.getNbPlacesRestantes();
        
        // Vérifier que le nouveau nombre de places est suffisant
        if (nbPlaces < placesReservees) {
            throw new IllegalArgumentException(
                "Impossible de réduire à " + nbPlaces + " places : " + placesReservees + " places déjà réservées"
            );
        }
        
        // Mettre à jour l'événement
        ev.setTitre(titre);
        ev.setDescription(description);
        ev.setDateEvenement(date);
        ev.setLieu(lieu);
        ev.setNbPlacesTotales(nbPlaces);
        ev.setNbPlacesRestantes(nbPlaces - placesReservees);
        ev.setPrixBase(prixBase);
        
        evenementDao.update(ev);
    }

    /**
     * Suppression d'un événement par son organisateur
     */
    public void supprimerEvenement(Organisateur org, long idEvenement) {
        Evenement ev = evenementDao.findById(idEvenement);
        
        if (ev == null) {
            throw new IllegalArgumentException("Événement introuvable");
        }
        
        // Vérifier que l'organisateur est bien le propriétaire
        if (!ev.getOrganisateur().getId().equals(org.getId())) {
            throw new IllegalArgumentException("Vous n'êtes pas autorisé à supprimer cet événement");
        }
        
        // Vérifier qu'il n'y a pas de réservations actives
        int placesReservees = ev.getNbPlacesTotales() - ev.getNbPlacesRestantes();
        if (placesReservees > 0) {
            throw new IllegalArgumentException(
                "Impossible de supprimer : " + placesReservees + " places sont déjà réservées"
            );
        }
        
        evenementDao.delete(idEvenement);
    }

    /**
     * Liste les événements d'un organisateur
     */
    public List<Evenement> listerEvenementsOrganisateur(Organisateur org) {
        return evenementDao.findAll().stream()
                .filter(e -> e.getOrganisateur().getId().equals(org.getId()))
                .toList();
    }
}
