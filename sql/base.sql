-- Création de la base de données
CREATE DATABASE IF NOT EXISTS gestion_budget;
USE gestion_budget;

-- Table des prévisions budgétaires
CREATE TABLE previsions (
    id_prevision INT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(255) NOT NULL,
    montant DECIMAL(10, 2) NOT NULL
);

-- Table des dépenses
CREATE TABLE depenses (
    id_depense INT AUTO_INCREMENT PRIMARY KEY,
    id_prevision INT NOT NULL,
    libelle VARCHAR(255) NOT NULL,
    montant DECIMAL(10, 2) NOT NULL,
    date_enregistrement DATE NOT NULL,
    FOREIGN KEY (id_prevision) REFERENCES previsions(id_prevision)
);

CREATE TABLE etat (
    id_etat INT AUTO_INCREMENT PRIMARY KEY,
    id_prevision INT NOT NULL,
    montant_prevu DECIMAL(10, 2) NOT NULL,
    montant_depense DECIMAL(10, 2) NOT NULL,
    montant_restant DECIMAL(10, 2) NOT NULL,
    date_mise_a_jour TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_prevision) REFERENCES previsions(id_prevision)
);

-- Vue pour l'état des prévisions (montant prévu, dépensé et restant)
CREATE VIEW etat_previsions AS
SELECT 
    p.id_prevision,
    p.libelle AS prevision_libelle,
    p.montant AS montant_prevu,
    COALESCE(SUM(d.montant), 0) AS montant_depense,
    p.montant - COALESCE(SUM(d.montant), 0) AS montant_restant
FROM 
    previsions p
LEFT JOIN 
    depenses d ON p.id_prevision = d.id_prevision
GROUP BY 
    p.id_prevision, p.libelle, p.montant;

-- Trigger pour vérifier que le montant de la dépense est valide
DELIMITER //
CREATE TRIGGER check_montant_depense BEFORE INSERT ON depenses
FOR EACH ROW
BEGIN
    DECLARE montant_restant DECIMAL(10, 2);
    
    -- Calculer le montant restant pour la prévision
    SELECT 
        p.montant - COALESCE(SUM(d.montant), 0) INTO montant_restant
    FROM 
        previsions p
    LEFT JOIN 
        depenses d ON p.id_prevision = d.id_prevision
    WHERE 
        p.id_prevision = NEW.id_prevision
    GROUP BY 
        p.id_prevision, p.montant;
    
    -- Vérifier si le montant de la dépense est valide
    IF NEW.montant > montant_restant THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Le montant de la dépense dépasse le budget restant pour cette prévision';
    END IF;
END //
DELIMITER ;

-- Exemples d'insertion de données
INSERT INTO previsions (libelle, montant) VALUES 
('Fournitures de bureau', 500.00),
('Matériel informatique', 2000.00),
('Frais de déplacement', 1500.00);

INSERT INTO depenses (id_prevision, libelle, montant, date_enregistrement) VALUES 
(1, 'Achat papier', 50.00, CURDATE()),
(1, 'Achat stylos', 30.00, CURDATE()),
(2, 'Achat écran', 500.00, CURDATE());