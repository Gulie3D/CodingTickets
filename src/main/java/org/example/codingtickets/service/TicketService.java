package org.example.codingtickets.service;

import org.example.codingtickets.model.*;

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
        // 1) Création des organisateurs
        Organisateur org1 = new Organisateur(
                userIdGen.getAndIncrement(),
                "Prof Java",
                "prof.java@coding.fr",
                "java123"
        );

        Organisateur org2 = new Organisateur(
                userIdGen.getAndIncrement(),
                "Prof Web",
                "prof.web@coding.fr",
                "web123"
        );

        // 2) Création des clients
        Client client1 = new Client(
                userIdGen.getAndIncrement(),
                "Alice Client",
                "alice@coding.fr",
                "alice123"
        );

        Client client2 = new Client(
                userIdGen.getAndIncrement(),
                "Bob Client",
                "bob@coding.fr",
                "bob123"
        );

        utilisateurs.add(org1);
        utilisateurs.add(org2);
        utilisateurs.add(client1);
        utilisateurs.add(client2);

        // 3) Création des événements d'exemple
        Evenement ev1 = new Evenement(
                eventIdGen.getAndIncrement(),
                "Atelier Java avancé",
                "Découverte des servlets et de JDBC",
                LocalDateTime.now().plusDays(7),
                "Salle 101",
                30,
                30,
                new BigDecimal("20.00"),
                org1
        );

        Evenement ev2 = new Evenement(
                eventIdGen.getAndIncrement(),
                "Soirée Spring Boot",
                "Introduction à Spring Boot",
                LocalDateTime.now().plusDays(10),
                "Salle 202",
                50,
                50,
                new BigDecimal("25.00"),
                org1
        );

        Evenement ev3 = new Evenement(
                eventIdGen.getAndIncrement(),
                "Workshop Front-End",
                "HTML/CSS/JS pour débutants",
                LocalDateTime.now().plusDays(5),
                "Lab Web",
                20,
                20,
                new BigDecimal("15.00"),
                org2
        );

        Evenement ev4 = new Evenement(
                eventIdGen.getAndIncrement(),
                "Crash Test Annulation",
                "Cet événement est aujourd'hui : impossible d'annuler !",
                LocalDateTime.now().plusHours(2), // A lieu dans 2 heures (donc < 24h)
                "Salle de Test",
                10,
                10,
                new BigDecimal("1.00"),
                org2
        );

        evenements.add(ev1);
        evenements.add(ev2);
        evenements.add(ev3);
        evenements.add(ev4);
    }


    public Utilisateur authentifier(String email, String motDePasse) {
        return utilisateurs.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email)
                        && u.getMotDePasse().equals(motDePasse))
                .findFirst()
                .orElse(null);
    }

    public List<Evenement> listerEvenements() {
        return new ArrayList<>(evenements); // copie pour éviter les modifs externes
    }

    public Evenement trouverEvenementParId(long id) {
        return evenements.stream()
                .filter(e -> e.getId() == id)
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
                .filter(r -> r.getId() == idReservation
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
}