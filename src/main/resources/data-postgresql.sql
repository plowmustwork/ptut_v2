-- Data for testing

-- Promotion
INSERT INTO promotion (nom_promotion) VALUES 
('FIE1'),
('FIE2'),
('FIE3'),
('FIA3'),
('FIE4'),
('FIA4'),
('FIE5'),
('FIA5');

-- Reset sequence for promotion (8 rows inserted)
SELECT setval('promotion_promotion_id_seq', (SELECT MAX(promotion_id) FROM promotion));

-- Annee
INSERT INTO annee (date_debut, date_fin) VALUES
(2023, 2024),
(2024, 2025),
(2025, 2026);

-- Reset sequence for annee (3 rows inserted)
SELECT setval('annee_annee_id_seq', (SELECT MAX(annee_id) FROM annee));

INSERT INTO annee_promotion (annee_id, promotion_id) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(1, 7),
(1, 8);

-- Enseignant
INSERT INTO enseignant (mail, nom, prenom, mot_de_passe) VALUES 
('testing@univ.fr', 'Doe', 'John', 'hashed_password_1');

-- Reset sequence for enseignant (1 row inserted)
SELECT setval('enseignant_enseignant_id_seq', (SELECT MAX(enseignant_id) FROM enseignant));