# CodingTickets App
CodingTickets est une application à un client de pouvoir réserver un événement ou l'annuler, et de permettre à un organisateur de créer un événement avec la date et le prix et le nombre de places.

## Installation et Configuration
Sur IntelliJ, lancer le mvn clean package, récupérer le fichier war apparaissant dans le target et le mettre dans le webapp de Tomcat en le renommant CodingTickets.war.
Dans le run configuration faire edit configuration et choisir Tomcat, indiquer dans l'URL http://localhost:8080/CodingTickets/login et dans le deployement faire le + et choisir CodingTickets:war exploded, et indiquer plus bas sur la même page dans application context /CodingTickets. 

## Technologies
- Spring Boot
- RabbitMQ
- PostgreSQL et Redis
- Meilisearch
- PipelinR

## Auteurs
Our developer's backend squad : 
- AMANI Liticia
- BASLAM Ismail
- BENSEGHIR Sabrina
- CAUSSE Julie 

## Product Backlog :

### Règle 1 – Capacité de l’événement
- R1.1 : Le nombre de places demandées doit être strictement positif.
- R1.2 : On ne peut pas réserver plus de places que nbPlacesRestantes pour un événement.
Conséquence : si nbPlaces > nbPlacesRestantes, la réservation est refusée.

### Règle 2 – Effet d’une réservation
Lorsqu’une réservation est validée :
- R2.1 : nbPlacesRestantes de l’événement diminue du nombre de places réservées.
- R2.2 : montantTotal de la réservation est calculé par la formule :

𝑚𝑜𝑛𝑡𝑎𝑛𝑡𝑇𝑜𝑡𝑎𝑙 = 𝑛𝑏𝑃𝑙𝑎𝑐𝑒𝑠 × 𝑝𝑟𝑖𝑥𝐵𝑎𝑠𝑒 
montantTotal = nbPlaces × prixBase

### Règle 3 – Annulation de réservation
- R3.1 : Une réservation ne peut être annulée que si l’événement n’est pas trop proche.
version simplifiée : annulation toujours possible
version “J-1” : annulation autorisée seulement si
dateEvenement >= (maintenant + 1 jour).

- R3.2 : Lors d’une annulation acceptée :
le statut de la réservation passe à ANNULEE,
nbPlacesRestantes de l’événement augmente du nombre de places annulées.
