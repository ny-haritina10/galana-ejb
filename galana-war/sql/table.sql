/*============================================= */
/*TABLE ======================================= */
/*============================================= */

-- Unit Table
CREATE TABLE Unit (
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Pompiste Table
CREATE TABLE Pompiste (
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Product Table
CREATE TABLE Product (
    id INT PRIMARY KEY,
    id_unit INT REFERENCES Unit(id),
    name VARCHAR(255) NOT NULL,
    PU_achat NUMBER NOT NULL,  
    PU_vente NUMBER NOT NULL   
);

-- Pompe 
CREATE TABLE Pompe (
    id INT PRIMARY KEY,
    id_product INT REFERENCES Product(id),
    name VARCHAR(255) NOT NULL,
    qte_max NUMBER NOT NULL,
    qte_initial NUMBER NOT NULL
);

-- Prelevement Table
CREATE TABLE Prelevement (
    id INT PRIMARY KEY,
    id_pompiste INT REFERENCES Pompiste(id),
    id_pompe INT REFERENCES Pompe(id),
    id_product INT REFERENCES Product(id),
    amount NUMBER NOT NULL,  
    date_prelevement DATE NOT NULL
);

-- Encaissement Table
CREATE TABLE Encaissement (
    id INT PRIMARY KEY,
    id_prelevement INT REFERENCES Prelevement(id),
    montant_encaisse NUMBER NOT NULL,
    date_encaissement DATE
);