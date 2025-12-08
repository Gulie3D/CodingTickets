# CodingTickets App partie 1
CodingTickets est une application à un client de pouvoir réserver un événement ou l'annuler, et de permettre à un organisateur de créer un événement avec la date et le prix et le nombre de places.

## Installation et Configuration
### Via IDE (IntelliJ ou Eclipse)
#### Configuration sur IntelliJ IDEA
1.  Ouvrez l'onglet **Maven** (à droite) > `Lifecycle`.
2.  Exécutez `clean` puis `package`.
3.  Vérifiez qu'un fichier `.war` a été créé dans le dossier `target`.
4.  Allez dans **Run > Edit Configurations**.
5.  Cliquez sur **+** et sélectionnez **Tomcat Server > Local**.
6.  Dans l'onglet **Deployment** :
    * Cliquez sur **+** > **Artifact**.
    * Sélectionnez `CodingTickets:war exploded`.
    * Dans **Application context**, mettez : `/CodingTickets`.
7.  Dans l'onglet **Server** :
    * URL : `http://localhost:8080/CodingTickets/login`
8.  Lancez le serveur (bouton Play vert).

#### Configuration sur Eclipse
1.  Faites **File > Import > Existing Maven Projects**.
2.  Clic droit sur le projet > **Run As > Maven build...** > Goals: `clean package`.
3.  Installez un serveur Tomcat 10/11 dans l'onglet **Servers**.
4.  Clic droit sur le serveur > **Add and Remove...** > Ajoutez `CodingTickets`.
5.  Démarrez le serveur et accédez à `http://localhost:8080/CodingTickets/login`.


## Technologies
- Tomcat 11.0.14
- Java 17
- IntelliJ ou Eclipse

## Auteurs
Equipe développeurs back-end : 
- AMANI Liticia
- BASLAM Ismail
- BENSEGHIR Sabrina
- CAUSSE Julie 

## Product Backlog : 
- En tant qu'utilisateur (client/organisateur), je veux pouvoir me connecter et me déconnecter.,
- En tant que client, je veux voir l'historique des évènements disponibles.,
- En tant que client, je veux pouvoir réserver des places.,
- En tant que client, je veux pouvoir consulter mes réservations.,
- En tant que client, je veux pouvoir annuler mes réservations.,
- En tant qu' organisateur, je veux pouvoir créer et gérer des évènements.

## Règles métier : 
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

## Modélisation et Conception
L'architecture de l'application repose sur une conception stricte séparant les données, les traitements et l'affichage.
### Du Diagramme de Classes vers la Couche Modèle (Java)
À partir du diagramme de classes UML, nous avons implémenté la couche **Model** (POJO) :
* **Encapsulation :** Toutes les entités (`Evenement`, `Reservation`) possèdent des attributs privés accessibles via des Getters/Setters.
* **Héritage :** La gestion des utilisateurs utilise l'héritage. 
* La classe mère `Utilisateur` est étendue par `Client` et `Organisateur`, 
* permettant de partager les attributs communs (nom, email, mot de passe) 
* tout en spécialisant les rôles via une Enum ou un discriminant.
## URLs Principales

L'application est accessible via les adresses suivantes (sur le port **8080** via IntelliJ/Eclipse) :

| Page | URL                                                        | Accès |
| :--- |:-----------------------------------------------------------| :--- |
| **Connexion** | `http://localhost:8080/CodingTickets/login`                | Public |
| **Liste des Événements** | `http://localhost:8080/CodingTickets/events`               | Public |
| **Mes Réservations** | `http://localhost:8080/CodingTickets/reservations/history` | Client uniquement |
| **Mes Événements** | `http://localhost:8080/CodingTickets/events/my`            | Organisateur uniquement |
| **Créer un Événement** | `http://localhost:8080/CodingTickets/events/create`        | Organisateur uniquement |
| **Déconnexion** | `http://localhost:8080/CodingTickets/logout`               | Tous |


## 🔐 Comptes de Test

Voici les identifiants pré-configurés pour tester l'application :

| Rôle | Email                 | Mot de passe |
| :--- |:----------------------|:-------------|
| **Organisateur** | `prof.java@coding.fr` | `java123`    |
| **Organisateur** | `prof.web@coding.fr`  | `web123`     |
| **Client** | `alice@coding.fr`     | `alice123`   |
| **Client** | `bob@coding.fr`       | `bob123`     |