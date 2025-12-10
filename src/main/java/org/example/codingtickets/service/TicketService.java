package org.example.codingtickets.service;

import org.example.codingtickets.model.*;
import org.example.codingtickets.utils.PasswordUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class TicketService {

    // Collections en mémoire
    private final List<Utilisateur> utilisateurs = new ArrayList<>();
    private final List<Evenement> evenements = new ArrayList<>();
    private final List<Reservation> reservations = new ArrayList<>();

    // Générateurs d'ID
    private final AtomicLong userIdGen = new AtomicLong(1);
    private final AtomicLong eventIdGen = new AtomicLong(1);
    private final AtomicLong reservationIdGen = new AtomicLong(1);

    public TicketService() {
        initialiserDonnees();
    }


       private void initialiserDonnees() {
        Organisateur orgBob = new Organisateur(
                userIdGen.getAndIncrement(),
                "Bob Organisateur",
                "bob_organisateur@coding.fr",
                PasswordUtils.hashPassword("bob123")
        );

        Organisateur orgMarie = new Organisateur(
                userIdGen.getAndIncrement(),
                "Marie Organisatrice",
                "marie_organisatrice@coding.fr",
                PasswordUtils.hashPassword("marie123")
        );

        Client clientAlice = new Client(
                userIdGen.getAndIncrement(),
                "Alice Cliente",
                "alice_cliente@coding.fr",
                PasswordUtils.hashPassword("alice123")
        );

        Client clientCharlie = new Client(
                userIdGen.getAndIncrement(),
                "Charlie Client",
                "charlie_client@coding.fr",
                PasswordUtils.hashPassword("charlie123")
        );

        Client clientDiana = new Client(
                userIdGen.getAndIncrement(),
                "Diana Cliente",
                "diana_cliente@coding.fr",
                PasswordUtils.hashPassword("diana123")
        );

        utilisateurs.add(orgBob);
        utilisateurs.add(orgMarie);
        utilisateurs.add(clientAlice);
        utilisateurs.add(clientCharlie);
        utilisateurs.add(clientDiana);


        // Event 1 (Bob) : Conférence Java Avancé (J+7)
        Evenement ev1 = new Evenement(
                eventIdGen.getAndIncrement(),
                "Conférence Java Avancé",
                "Tout savoir sur les Threads et JDBC",
                LocalDateTime.now().plusDays(7),
                "Salle Turing",
                50,
                48, // 2 places prises par la réservation d'Alice
                new BigDecimal("30.00"),
                orgBob
        );

        // Event 2 (Bob) : Hackathon Express (H+3)
        Evenement ev2 = new Evenement(
                eventIdGen.getAndIncrement(),
                "Hackathon Express",
                "Événement imminent ! Impossible d'annuler si on réserve maintenant.",
                LocalDateTime.now().plusHours(3),
                "Salle Lovelace",
                20,
                20,
                new BigDecimal("10.00"),
                orgBob
        );

        // Event 3 (Marie) : Workshop Docker & Kubernetes (J+14)
        Evenement ev3 = new Evenement(
                eventIdGen.getAndIncrement(),
                "Workshop Docker & Kubernetes",
                "Apprenez à conteneuriser vos applications Java",
                LocalDateTime.now().plusDays(14),
                "Salle Ada",
                30,
                28, // 2 places prises par la réservation de Charlie
                new BigDecimal("45.00"),
                orgMarie
        );

        // Event 4 (Marie) : Meetup Spring Boot (J+21)
        Evenement ev4 = new Evenement(
                eventIdGen.getAndIncrement(),
                "Meetup Spring Boot",
                "Découverte de Spring Boot 3 et ses nouveautés",
                LocalDateTime.now().plusDays(21),
                "Amphithéâtre Grace Hopper",
                100,
                95,
                new BigDecimal("15.00"),
                orgMarie
        );

        evenements.add(ev1);
        evenements.add(ev2);
        evenements.add(ev3);
        evenements.add(ev4);


        // Réservation 1 : Alice sur "Conférence Java" (-2 jours)
        Reservation res1 = new Reservation(
                reservationIdGen.getAndIncrement(),
                LocalDateTime.now().minusDays(2),
                2,
                new BigDecimal("60.00"), // 2 * 30.00
                StatutReservation.CONFIRMEE,
                clientAlice,
                ev1
        );

        // Réservation 2 : Charlie sur "Workshop Docker" (-1 jour)
        Reservation res2 = new Reservation(
                reservationIdGen.getAndIncrement(),
                LocalDateTime.now().minusDays(1),
                2,
                new BigDecimal("90.00"), // 2 * 45.00
                StatutReservation.CONFIRMEE,
                clientCharlie,
                ev3
        );

        reservations.add(res1);
        reservations.add(res2);
    }


    public Utilisateur authentifier(String email, String motDePasse) {
        return utilisateurs.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email)
                        && PasswordUtils.checkPassword(motDePasse, u.getMotDePasse()))
                .findFirst()
                .orElse(null);
    }

    public List<Evenement> listerEvenements() {
        return new ArrayList<>(evenements); // copie pour éviter les modifs externes
    }

    public Evenement trouverEvenementParId(long id) {
        return evenements.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Reservation reserver(Client client, long idEvenement, int nbPlaces) {
        Evenement evenement = trouverEvenementParId(idEvenement);
        if (evenement == null) {
            throw new IllegalArgumentException("Événement introuvable");
        }

        evenement.reserverPlaces(nbPlaces);

        BigDecimal montantTotal =
                evenement.getPrixBase().multiply(BigDecimal.valueOf(nbPlaces));

        Reservation reservation = new Reservation(
                reservationIdGen.getAndIncrement(),
                LocalDateTime.now(),
                nbPlaces,
                montantTotal,
                StatutReservation.CONFIRMEE,
                client,
                evenement
        );

        reservations.add(reservation);
        return reservation;
    }

    public List<Reservation> listerReservationsClient(Client client) {
        return reservations.stream()
                .filter(r -> r.getClient().getId().equals(client.getId()))
                .collect(Collectors.toList());
    }

    public void annulerReservation(long idReservation, Client client) {
        Optional<Reservation> opt = reservations.stream()
                .filter(r -> r.getId().equals(idReservation)
                        && r.getClient().getId().equals(client.getId()))
                .findFirst();

        if (opt.isEmpty()) {
            throw new IllegalArgumentException(
                    "Réservation introuvable ou non liée à ce client");
        }

        Reservation r = opt.get();
        r.annuler(LocalDateTime.now());
    }

    //pour les organisateurs
    public Evenement creerEvenement(Organisateur org,
                                    String titre,
                                    String description,
                                    LocalDateTime date,
                                    String lieu,
                                    int nbPlaces,
                                    BigDecimal prixBase) {
        Evenement ev = new Evenement(
                eventIdGen.getAndIncrement(),
                titre,
                description,
                date,
                lieu,
                nbPlaces,
                nbPlaces,
                prixBase,
                org
        );
        evenements.add(ev);
        return ev;
    }

    /**
     * Modification d'un événement par son organisateur
     */
    public void modifierEvenement(Organisateur org, long idEvenement, String titre, String description,
                                  LocalDateTime date, String lieu, int nbPlaces, BigDecimal prixBase) {
        Evenement ev = trouverEvenementParId(idEvenement);

        if (ev == null) {
            throw new IllegalArgumentException("Événement introuvable");
        }

        if (!ev.getOrganisateur().getId().equals(org.getId())) {
            throw new IllegalArgumentException("Vous n'êtes pas autorisé à modifier cet événement");
        }

        int placesReservees = ev.getNbPlacesTotales() - ev.getNbPlacesRestantes();

        if (nbPlaces < placesReservees) {
            throw new IllegalArgumentException(
                    "Impossible de réduire à " + nbPlaces + " places : " + placesReservees + " places déjà réservées"
            );
        }

        ev.setTitre(titre);
        ev.setDescription(description);
        ev.setDateEvenement(date);
        ev.setLieu(lieu);
        ev.setNbPlacesTotales(nbPlaces);
        ev.setNbPlacesRestantes(nbPlaces - placesReservees);
        ev.setPrixBase(prixBase);

    }

    /**
     * Suppression d'un événement par son organisateur
     */
    public void supprimerEvenement(Organisateur org, long idEvenement) {
        Evenement ev = trouverEvenementParId(idEvenement);

        if (ev == null) {
            throw new IllegalArgumentException("Événement introuvable");
        }

        if (!ev.getOrganisateur().getId().equals(org.getId())) {
            throw new IllegalArgumentException("Vous n'êtes pas autorisé à supprimer cet événement");
        }

        int placesReservees = ev.getNbPlacesTotales() - ev.getNbPlacesRestantes();
        if (placesReservees > 0) {
            throw new IllegalArgumentException(
                    "Impossible de supprimer : " + placesReservees + " places sont déjà réservées"
            );
        }
        evenements.remove(ev);
    }
    /**
     * Liste les événements créés par un organisateur spécifique.
     * @param org L'organisateur connecté
     * @return La liste de ses événements
     */
    public List<Evenement> listerEvenementsOrganisateur(Organisateur org) {
        return evenements.stream()
                .filter(e -> e.getOrganisateur().getId().equals(org.getId()))
                .collect(Collectors.toList());
    }
}
