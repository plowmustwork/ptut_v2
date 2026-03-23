
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
(1, 1),  -- FIE1 -> 2023-2024
(1, 2),  -- FIE2 -> 2023-2024
(1, 3),  -- FIE3 -> 2023-2024
(1, 4),  -- FIA3 -> 2023-2024
(1, 5),  -- FIE4 -> 2023-2024
(1, 6),  -- FIA4 -> 2023-2024
(1, 7),  -- FIE5 -> 2023-2024
(1, 8),  -- FIA5 -> 2023-2024
(2, 3),  -- FIE3 -> 2024-2025 
(3, 3);  -- FIE3 -> 2025-2026 

INSERT INTO semestre (nom_semestre, promotion_id, annee_id) VALUES
('S1', 1, 1),  -- FIE1 -> 2023-2024
('S2', 1, 1),  -- FIE1 -> 2023-2024
('S3', 2, 1),  -- FIE2 -> 2023-2024
('S4', 2, 1),  -- FIE2 -> 2023-2024
('S5', 3, 2),  -- FIE3 -> 2024-2025 
('S6', 3, 2),  -- FIE3 -> 2024-2025 
('S5', 3, 3),  -- FIE3 -> 2025-2026 
('S6', 3, 3),  -- FIE3 -> 2025-2026 
('S5', 4, 2),  -- FIA3 -> 2024-2025
('S6', 4, 2),  -- FIA3 -> 2024-2025
('S5', 4, 3),  -- FIA3 -> 2025-2026
('S6', 4, 3);  -- FIA3 -> 2025-2026