-- =============================================
-- SCRIPT D'INITIALISATION - CodingTickets
-- =============================================

-- 1. NETTOYAGE
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS evenement;
DROP TABLE IF EXISTS utilisateur;

-- 2. CRÉATION DE LA TABLE UTILISATEUR
CREATE TABLE utilisateur (
    id_utilisateur SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    motdepasse VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL CHECK (role IN ('CLIENT', 'ORGANISATEUR'))
);

-- 3. CRÉATION DE LA TABLE EVENEMENT
CREATE TABLE evenement (
    id_evenement SERIAL PRIMARY KEY,
    titre VARCHAR(150) NOT NULL,
    description TEXT,
    dateevenement TIMESTAMP NOT NULL,
    lieu VARCHAR(255) NOT NULL,
    nbreplacetotale INT NOT NULL,
    nbreplacesrestantes INT NOT NULL,
    prix_base DECIMAL(10, 2) NOT NULL,
    id_organisateur INT NOT NULL,
    CONSTRAINT fk_event_orga
        FOREIGN KEY (id_organisateur)
            REFERENCES utilisateur(id_utilisateur)
            ON DELETE CASCADE
);

-- 4. CRÉATION DE LA TABLE RESERVATION
CREATE TABLE reservation (
    id_reservation SERIAL PRIMARY KEY,
    datereservation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    nbredeplace INT NOT NULL,
    montanttotal DECIMAL(10, 2) NOT NULL,
    statutenum VARCHAR(50) NOT NULL,
    id_client INT NOT NULL,
    id_evenement INT NOT NULL,
    CONSTRAINT fk_res_client
        FOREIGN KEY (id_client)
            REFERENCES utilisateur(id_utilisateur)
            ON DELETE CASCADE,
    CONSTRAINT fk_res_event
        FOREIGN KEY (id_evenement)
            REFERENCES evenement(id_evenement)
            ON DELETE CASCADE
);

-- =============================================
-- INSERTION DES DONNÉES DE TEST
-- =============================================

-- UTILISATEURS : 2 Organisateurs + 3 Clients
INSERT INTO utilisateur (nom, email, motdepasse, role) VALUES
    ('Bob Organisateur', 'bob_organisateur@coding.fr', 'bob123', 'ORGANISATEUR'),
    ('Marie Organisatrice', 'marie_organisatrice@coding.fr', 'marie123', 'ORGANISATEUR'),
    ('Alice Cliente', 'alice_cliente@coding.fr', 'alice123', 'CLIENT'),
    ('Charlie Client', 'charlie_client@coding.fr', 'charlie123', 'CLIENT'),
    ('Diana Cliente', 'diana_cliente@coding.fr', 'diana123', 'CLIENT');

-- ÉVÉNEMENTS : 4 événements
INSERT INTO evenement (titre, description, dateevenement, lieu, nbreplacetotale, nbreplacesrestantes, prix_base, id_organisateur) VALUES
    ('Conférence Java Avancé', 
     'Tout savoir sur les Threads et JDBC', 
     CURRENT_TIMESTAMP + INTERVAL '7 days', 
     'Salle Turing', 
     50, 48, 30.00, 1),
    
    ('Hackathon Express', 
     'Événement imminent ! Impossible d annuler si on réserve maintenant.', 
     CURRENT_TIMESTAMP + INTERVAL '3 hours', 
     'Salle Lovelace', 
     20, 20, 10.00, 1),
    
    ('Workshop Docker & Kubernetes', 
     'Apprenez à conteneuriser vos applications Java', 
     CURRENT_TIMESTAMP + INTERVAL '14 days', 
     'Salle Ada', 
     30, 28, 45.00, 2),
    
    ('Meetup Spring Boot', 
     'Découverte de Spring Boot 3 et ses nouveautés', 
     CURRENT_TIMESTAMP + INTERVAL '21 days', 
     'Amphithéâtre Grace Hopper', 
     100, 95, 15.00, 2);

-- RÉSERVATIONS : 2 réservations existantes
INSERT INTO reservation (datereservation, nbredeplace, montanttotal, statutenum, id_client, id_evenement) VALUES
    (CURRENT_TIMESTAMP - INTERVAL '2 days', 2, 60.00, 'CONFIRMEE', 3, 1),
    (CURRENT_TIMESTAMP - INTERVAL '1 day', 2, 90.00, 'CONFIRMEE', 4, 3);
