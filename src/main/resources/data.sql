-- 1. NETTOYAGE
TRUNCATE TABLE reservation, evenement, utilisateur RESTART IDENTITY CASCADE;

-- 2. CRÉATION DES UTILISATEURS
INSERT INTO utilisateur (nom, email, motdepasse, role)
VALUES ('Bob Organisateur', 'bob_organisateur@coding.fr', 'bob123', 'ORGANISATEUR');

INSERT INTO utilisateur (nom, email, motdepasse, role)
VALUES ('Alice Cliente', 'alice_cliente@coding.fr', 'alice123', 'CLIENT');

-- 3. CRÉATION DES ÉVÉNEMENTS
INSERT INTO evenement (titre, description, dateevenement, lieu, nbreplacetotale, nbreplacesrestantes, prix_base, id_organisateur)
VALUES (
           'Conférence Java Avancé',
           'Tout savoir sur les Threads et JDBC',
           CURRENT_TIMESTAMP + INTERVAL '7 days',
           'Salle Turing',
           50,
           50, -- Stock complet !
           30.00,
           1
       );

-- Événement 2 : Dans 3 heures
INSERT INTO evenement (titre, description, dateevenement, lieu, nbreplacetotale, nbreplacesrestantes, prix_base, id_organisateur)
VALUES (
           'Hackathon Express',
           'Événement imminent ! Impossible d annuler si on réserve maintenant.',
           CURRENT_TIMESTAMP + INTERVAL '3 hours',
           'Salle Lovelace',
           20,
           20, -- Stock complet !
           10.00,
           1
       );