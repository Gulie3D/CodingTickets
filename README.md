R1 – Gestion des capacités / disponibilité :

Ces règles définissent ce qui est autorisé ou non lors d’une réservation.
R1.1 – Nombre de places valide
Le nombre de places demandées (nbPlaces) doit être strictement supérieur à 0.

R1.2 – Capacité restante
Il est impossible de réserver plus de places que le nombre de places encore disponibles (nbPlacesRestantes) pour un événement.
Conséquence :
Si nbPlaces > nbPlacesRestantes, la réservation est refusée via une exception métier.

R2 – Effets d’une réservation

Ces règles définissent ce qui se produit lorsqu’une réservation est acceptée.
R2.1 – Décrémentation des places restantes
Lors d’une réservation validée, le nombre de places restantes de l’événement diminue du nombre de places réservées.

R2.2 – Calcul du montant total
Le prix total d'une réservation est calculé comme suit :

montantTotal = 𝑛𝑏𝑃𝑙𝑎𝑐𝑒𝑠 × 𝑝𝑟𝑖𝑥𝐵𝑎𝑠𝑒
montantTotal = nbPlaces × prixBase

R3 – Annulation d’une réservation

Ces règles concernent les conditions d’annulation et leurs effets.
R3.1 – Conditions temporelles
Une réservation ne peut être annulée que si l’événement n’est pas trop proche.
Dans ce projet, nous retenons la règle :
Annulation autorisée si l’événement est à au moins J+1.
Autrement dit :
dateEvenement ≥ maintenant + 1jour / dateEvenement ≥ maintenant + 1jour

R3.2 – Effets d’une annulation
Lorsqu'une annulation est acceptée :
le statut de la réservation passe à ANNULEE,
le nombre de places restantes de l’événement augmente du nombre de places annulées.

Exceptions métier :
Deux exceptions spécifiques ont été définies pour représenter les violations de règles métier.
PlacesInsuffisantesException
Lancée lorsque l’utilisateur demande plus de places que disponibles :

public class PlacesInsuffisantesException extends RuntimeException {
    public PlacesInsuffisantesException(String message) {
        super(message);
    }
}

AnnulationTardiveException :
Lancée lorsqu’une annulation est effectuée trop tard (événement trop proche).

public class AnnulationTardiveException extends RuntimeException {
    public AnnulationTardiveException(String message) {
        super(message);
    }
}

Implémentation des règles dans le code :
Cette section présente les méthodes clés où les règles métier sont appliquées.
3.1 Méthode reserverPlaces (classe Evenement)
Gère la disponibilité des places.

public void reserverPlaces(int nb) {
    if (nb <= 0) {
        throw new IllegalArgumentException("Le nombre de places doit être > 0");
    }
    if (nb > nbPlacesRestantes) {
        throw new PlacesInsuffisantesException(
                "Il ne reste que " + nbPlacesRestantes + " place(s) pour cet événement."
        );
    }
    nbPlacesRestantes -= nb; // R2.1
} 

Méthode annulerPlaces (classe Evenement) :
Permet de réattribuer les places lors d’une annulation.

public void annulerPlaces(int nb) {
    nbPlacesRestantes += nb;
    if (nbPlacesRestantes > nbPlacesTotales) {
        nbPlacesRestantes = nbPlacesTotales;
    }
}

Méthode annuler (classe Reservation):
Encapsule la logique d’annulation (R3.1 + R3.2).

public void annuler(LocalDateTime maintenant) {
    if (statut == StatutReservation.ANNULEE) {
        return; // déjà annulée
    }

    LocalDateTime limite = maintenant.plusDays(1);
    if (evenement.getDateEvenement().isBefore(limite)) {
        throw new AnnulationTardiveException(
                "Annulation impossible : l'événement est trop proche."
        );
    }

    statut = StatutReservation.ANNULEE;
    evenement.annulerPlaces(nbPlaces); // R3.2
} 

Méthode reserver (classe TicketService):
Implémente la logique de réservation depuis le service.

public Reservation reserver(Client client, long idEvenement, int nbPlaces) {
    Evenement evenement = trouverEvenementParId(idEvenement);
    if (evenement == null) {
        throw new IllegalArgumentException("Événement introuvable");
    }

    evenement.reserverPlaces(nbPlaces); // R1 + R2.1

    BigDecimal montantTotal =
            evenement.getPrixBase().multiply(BigDecimal.valueOf(nbPlaces)); // R2.2

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

Méthode annulerReservation (classe TicketService):
Validation côté service et délégation à Reservation.annuler().

public void annulerReservation(long idReservation, Client client) {
    Optional<Reservation> opt = reservations.stream()
            .filter(r -> r.getId() == idReservation
                      && r.getClient().getId().equals(client.getId()))
            .findFirst();

    if (opt.isEmpty()) {
        throw new IllegalArgumentException("Réservation introuvable ou non liée à ce client");
    }

    Reservation r = opt.get();
    r.annuler(LocalDateTime.now()); // R3.1 + R3.2
}
