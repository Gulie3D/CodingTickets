package org.example.codingtickets.service;

import org.example.codingtickets.dao.EvenementDAO;
import org.example.codingtickets.dao.ReservationDAO;
import org.example.codingtickets.dao.UtilisateurDAO;
import org.example.codingtickets.dao.jdbc.JdbcEvenementDAO;
import org.example.codingtickets.dao.jdbc.JdbcReservationDAO;
import org.example.codingtickets.dao.jdbc.JdbcUtilisateurDAO;
import org.example.codingtickets.model.*;

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
        return utilisateurDao.findByEmailAndPassword(email, motDePasse);
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
     * 1. Récupère l'événement
     * 2. Vérifie et décrémente les places (Logique métier)
     * 3. Met à jour l'événement en base
     * 4. Sauvegarde la réservation en base
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
                0L, // ID temporaire, ignoré par le save
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
        evt.setNbPlacesRestantes(evt.getNbPlacesRestantes() + r.getNbPlaces());
        evenementDao.update(evt);
    }


    /**
     * Création d'événement pour les organisateurs
     */
    public void creerEvenement(Organisateur org, String titre, String description,
                               LocalDateTime date, String lieu, int nbPlaces, BigDecimal prixBase) {
        Evenement ev = new Evenement(
                1L, // ID généré par la BDD
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

    public List<Evenement> listerEvenementsOrganisateur(Organisateur org) {
        return evenementDao.findAll().stream()
                .filter(e -> e.getOrganisateur().getId().equals(org.getId()))
                .toList();
    }
}