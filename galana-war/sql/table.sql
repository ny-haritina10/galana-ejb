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
    PU_vente NUMBER NOT NULL,
    type_product VARCHAR(255) DEFAULT 'UNDEFINED',
    sous_type VARCHAR(255) DEFAULT 'UNDEFINED', 
    qte_initial DECIMAL(10, 2) DEFAULT 0
);

ALTER TABLE Product
ADD sous_type VARCHAR(255) DEFAULT 'UNDEFINED';

-- Pompe (Cuve = Pompe)
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

/*----------------------------- */
/* SUJET II ------------------ */
/*----------------------------- */

-- CuveReference Table
CREATE TABLE CuveReference (
    id INT PRIMARY KEY,
    id_pompe INT REFERENCES Pompe(id),
    quantity NUMBER NOT NULL,
    mesure NUMBER NOT NULL
);

-- Mesurement Table
CREATE TABLE Mesurement (
    id INT PRIMARY KEY,
    id_pompe INT REFERENCES Pompe(id),
    date_mesurement DATE NOT NULL,
    mesure NUMBER NOT NULL
);

/*----------------------------- */
/* SUJET III ------------------ */
/*----------------------------- */

-- Creance Table
CREATE TABLE Creance (
    id INT PRIMARY KEY,
    id_prelevement REFERENCES Prelevement(id),
    id_client VARCHAR(255) NOT NULL,
    date_echeance DATE NOT NULL,
    amount NUMBER NOT NULL
);

/*----------------------------- */
/* SUJET IV ------------------- */
/*----------------------------- */

-- Orders Table (Commande) 
CREATE TABLE Orders (
    id INT PRIMARY KEY,
    id_client VARCHAR(255) NOT NULL,        -- from centrale db that's why there is no FK
    date_order DATE NOT NULL
);

-- Orders Items (Produits commandés)
CREATE TABLE OrderItems (
    id INT PRIMARY KEY,
    id_order REFERENCES Orders(id),
    id_product REFERENCES Product(id),
    quantity NUMBER CHECK (quantity > 0)
);

-- Invoices Table (Facture)
CREATE TABLE SaleInvoices (
    id INT PRIMARY KEY,
    id_order REFERENCES Orders(id),
    total_amount NUMBER CHECK(total_amount >= 0)
);

ALTER TABLE SaleInvoices
ADD invoice_type VARCHAR(255) DEFAULT 'PAYE';


-- Stock
CREATE TABLE Stock (
    id INT PRIMARY KEY,
    id_product REFERENCES Product(id),
    date_session DATE NOT NULL,  
    quantity_in NUMBER CHECK (quantity_in >= 0),
    quantity_out NUMBER CHECK (quantity_out >= 0)
);


/*----------------------------- */
/* SUJET IV ------------------- */
/*----------------------------- */