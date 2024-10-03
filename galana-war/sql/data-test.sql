INSERT INTO Unit (id, name) VALUES (seq_unit.NEXTVAL, 'Liter');

INSERT INTO Pompiste (id, name) VALUES (seq_pompiste.NEXTVAL, 'John Doe');
INSERT INTO Pompiste (id, name) VALUES (seq_pompiste.NEXTVAL, 'Jane Smith');

INSERT INTO Product (id, id_unit, name, PU_achat, PU_vente) VALUES (seq_product.NEXTVAL, 2, 'Essence', 1.10, 1.50);

INSERT INTO Pompe (id, id_product, name, qte_max, qte_initial) VALUES (seq_cuve.NEXTVAL, 2, 'Cuve 1', 5000, 500);
INSERT INTO Pompe (id, id_product, name, qte_max, qte_initial) VALUES (seq_cuve.NEXTVAL, 2, 'Cuve 2', 6000, 200);
INSERT INTO Pompe (id, id_product, name, qte_max, qte_initial) VALUES (seq_cuve.NEXTVAL, 2, 'Cuve 3', 7000, 100);