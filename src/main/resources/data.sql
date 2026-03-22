
-- Data for testing

-- Annee
INSERT INTO annee (annee_debut, annee_fin) VALUES
('2023', '2024'),
('2024', '2025'),
('2025', '2026');

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


INSERT INTO annee_promotion (annee_id, promotion_id) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(1, 7),
(1, 8);


--Enseignant
INSERT INTO enseignant (mail, nom, prenom, mot_de_passe) VALUES 
('testing@univ.fr', 'Doe', 'John', 'hashed_password_1');

