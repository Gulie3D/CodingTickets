# CodingTickets App
**CodingTickets** est une application web Java JEE permettant la gestion d'événements. Elle offre une plateforme à deux facettes :
* **Côté Client :** Consultation, réservation et annulation de places pour des événements.
* **Côté Organisateur :** Création et gestion complète d'événements (date, lieu, prix, capacité).


## Installation et Configuration

### Option A : Via Docker (Recommandé)
Cette méthode lance à la fois l'application et la base de données sans configuration manuelle.

1.  Assurez-vous d'avoir **Docker** et **Docker Compose** installés.
2.  À la racine du projet, ouvrez un terminal.
3.  Lancez la commande suivante pour construire et démarrer les conteneurs :
    ```bash
    docker-compose up --build
    ```
4.  L'application sera accessible sur : `http://localhost:8888/CodingTickets/login`

> **Note :** Le script d'initialisation de la base de données (`init.sql`) est exécuté automatiquement au premier lancement pour créer les tables et les utilisateurs de test.

### Option B : Via IDE (IntelliJ ou Eclipse)

#### Prérequis Base de données
Pour cette méthode, vous devez avoir un serveur PostgreSQL local actif sur le port `5432`.
Si la base est vide, exécutez manuellement le script `init.sql` situé à la racine du projet dans votre outil SQL (pgAdmin ou console).

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
* **Langage :** Java 17
* **Serveur d'application :** Apache Tomcat 11.0.14
* **Base de données :** PostgreSQL
* **Conteneurisation :** Docker & Docker Compose
* **Architecture :** MVC (Modèle-Vue-Contrôleur) sans framework (Servlets/JSP/JDBC)
* **IDE recommandés :** IntelliJ IDEA ou Eclipse

## Auteurs
Equipe développeurs back-end : 
- AMANI Liticia
- BASLAM Ismail
- BENSEGHIR Sabrina
- CAUSSE Julie 

## Product Backlog :
* **En tant qu'utilisateur (Client/Organisateur) :**
    * Je veux pouvoir me connecter et me déconnecter.
* **En tant que Client :**
    * Je veux voir l'historique des événements disponibles.
    * Je veux pouvoir réserver des places.
    * Je veux pouvoir consulter mes réservations.
    * Je veux pouvoir annuler mes réservations.
* **En tant qu'Organisateur :**
    * Je veux pouvoir créer et gérer des événements.

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

### 1. Du Diagramme de Classes vers la Couche Modèle (Java)
À partir du diagramme de classes UML, nous avons implémenté la couche **Model** (POJO) :
* **Encapsulation :** Toutes les entités (`Evenement`, `Reservation`) possèdent des attributs privés accessibles via des Getters/Setters.
* **Héritage :** La gestion des utilisateurs utilise l'héritage. La classe mère `Utilisateur` est étendue par `Client` et `Organisateur`, permettant de partager les attributs communs (nom, email, mot de passe) tout en spécialisant les rôles via une Enum ou un discriminant.

### 2. Du MCD/MPD vers la Base de Données (SQL)
Le Modèle Conceptuel de Données (MCD) a été traduit en Modèle Physique (MPD) pour définir le schéma relationnel PostgreSQL :
* **Entités & Tables :** Chaque classe métier correspond à une table (`utilisateur`, `evenement`, `reservation`).
* **Relations & Cardinalités :**
    * **Un Organisateur crée plusieurs Événements (1,n) :** Traduit par une clé étrangère `id_organisateur` dans la table `evenement`.
    * **Un Client effectue plusieurs Réservations (0,n) :** Traduit par une clé étrangère `id_client` dans la table `reservation`.
    * **Un Événement reçoit plusieurs Réservations (0,n) :** Traduit par une clé étrangère `id_evenement` dans la table `reservation`.
* **Contraintes d'intégrité :** Utilisation de `ON DELETE CASCADE` pour maintenir la cohérence des données lors de la suppression d'un utilisateur ou d'un événement.

---

## URLs Principales

L'application est accessible via les adresses suivantes (sur le port **8888** avec Docker, ou **8080** via IntelliJ/Eclipse) :

| Page | URL (Docker) | Accès |
| :--- | :--- | :--- |
| **Connexion** | `http://localhost:8888/CodingTickets/login` | Public |
| **Liste des Événements** | `http://localhost:8888/CodingTickets/events` | Public |
| **Mes Réservations** | `http://localhost:8888/CodingTickets/reservations/history` | Client uniquement |
| **Mes Événements** | `http://localhost:8888/CodingTickets/events/my` | Organisateur uniquement |
| **Créer un Événement** | `http://localhost:8888/CodingTickets/events/create` | Organisateur uniquement |
| **Déconnexion** | `http://localhost:8888/CodingTickets/logout` | Tous |


## Comptes de Test

Voici les identifiants pré-configurés pour tester l'application :

| Rôle | Email | Mot de passe |
| :--- | :--- | :--- |
| **Organisateur** | `bob_organisateur@coding.fr` | `bob123` |
| **Client** | `alice_cliente@coding.fr` | `alice123` |

