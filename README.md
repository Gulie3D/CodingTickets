Règle 1 – Capacité de l’événement

R1.1 : Le nombre de places demandées doit être strictement positif.
R1.2 : On ne peut pas réserver plus de places que nbPlacesRestantes pour un événement.
Conséquence : si nbPlaces > nbPlacesRestantes, la réservation est refusée.

Règle 2 – Effet d’une réservation

Lorsqu’une réservation est validée :
R2.1 : nbPlacesRestantes de l’événement diminue du nombre de places réservées.
R2.2 : montantTotal de la réservation est calculé par la formule :

𝑚𝑜𝑛𝑡𝑎𝑛𝑡𝑇𝑜𝑡𝑎𝑙 = 𝑛𝑏𝑃𝑙𝑎𝑐𝑒𝑠 × 𝑝𝑟𝑖𝑥𝐵𝑎𝑠𝑒 
montantTotal = nbPlaces × prixBase

Règle 3 – Annulation de réservation

R3.1 : Une réservation ne peut être annulée que si l’événement n’est pas trop proche.
version simplifiée : annulation toujours possible
version “J-1” : annulation autorisée seulement si
dateEvenement >= (maintenant + 1 jour).

R3.2 : Lors d’une annulation acceptée :
le statut de la réservation passe à ANNULEE,
nbPlacesRestantes de l’événement augmente du nombre de places annulées.
