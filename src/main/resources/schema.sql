
-- 1. NETTOYAGE
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS evenement;
DROP TABLE IF EXISTS utilisateur;

-- 2. CRÉATION DE LA TABLE UTILISATEUR
CREATE TABLE utilisateur (
                             id_utilisateur SERIAL PRIMARY KEY,
                             nom VARCHAR(100) NOT NULL,
                             email VARCHAR(255) UNIQUE NOT NULL, -- Email unique obligatoire
                             motdepasse VARCHAR(255) NOT NULL,
                             role VARCHAR(50) NOT NULL CHECK (role IN ('CLIENT', 'ORGANISATEUR')) -- Sécurité supplémentaire
);

-- 3. CRÉATION DE LA TABLE EVENEMENT
CREATE TABLE evenement (
                           id_evenement SERIAL PRIMARY KEY,
                           titre VARCHAR(150) NOT NULL,
                           description TEXT,
                           dateevenement TIMESTAMP NOT NULL, -- TIMESTAMP pour gérer date + heure
                           lieu VARCHAR(255) NOT NULL,
                           nbreplacetotale INT NOT NULL,
                           nbreplacesrestantes INT NOT NULL,
                           prix_base DECIMAL(10, 2) NOT NULL, -- DECIMAL est mieux pour l'argent
                           id_organisateur INT NOT NULL,

    -- Lien vers l'utilisateur (qui doit être un organisateur)
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
                             statutenum VARCHAR(50) NOT NULL, -- 'CONFIRMEE', 'ANNULEE'
                             id_client INT NOT NULL,
                             id_evenement INT NOT NULL,

    -- Lien vers le client
                             CONSTRAINT fk_res_client
                                 FOREIGN KEY (id_client)
                                     REFERENCES utilisateur(id_utilisateur)
                                     ON DELETE CASCADE,

    -- Lien vers l'événement
                             CONSTRAINT fk_res_event
                                 FOREIGN KEY (id_evenement)
                                     REFERENCES evenement(id_evenement)
                                     ON DELETE CASCADE
);
