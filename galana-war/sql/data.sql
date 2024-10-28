-- UNIT
INSERT INTO Unit (id, name) VALUES (seq_unit.NEXTVAL, 'Liter');
INSERT INTO Unit (id, name) VALUES (seq_unit.NEXTVAL, 'NB');

-- POMPISTE
INSERT INTO Pompiste (id, name) VALUES (seq_pompiste.NEXTVAL, 'John Doe');
INSERT INTO Pompiste (id, name) VALUES (seq_pompiste.NEXTVAL, 'Jane Smith');

-- PRODUCT
INSERT INTO Product (id, id_unit, name, PU_achat, PU_vente) VALUES (seq_product.NEXTVAL, 2, 'Essence', 1.10, 2);
INSERT INTO Product (id, id_unit, name, PU_achat, PU_vente, type_product, qte_initial) VALUES (seq_product.NEXTVAL, 21, 'Coca', 15, 17, 'BOUTIQUE', 50);
INSERT INTO Product (id, id_unit, name, PU_achat, PU_vente, type_product, qte_initial) VALUES (seq_product.NEXTVAL, 21, 'Doritos', 9, 11, 'BOUTIQUE', 150);
INSERT INTO Product (id, id_unit, name, PU_achat, PU_vente, type_product, qte_initial) VALUES (seq_product.NEXTVAL, 21, 'Eau Vive', 5, 6, 'BOUTIQUE', 20);
INSERT INTO Product (id, id_unit, name, PU_achat, PU_vente, type_product, qte_initial, sous_type) VALUES (seq_product.NEXTVAL, 21, 'Lubrifiant Essence', 2, 4, 'MIXTE', 50, 'ESSENCE');
INSERT INTO Product (id, id_unit, name, PU_achat, PU_vente, type_product, qte_initial, sous_type) VALUES (seq_product.NEXTVAL, 21, 'Lubrifiant Gasoil', 3, 5, 'MIXTE', 50, 'GASOIL');

-- POMPE
INSERT INTO Pompe (id, id_product, name, qte_max, qte_initial) VALUES (seq_cuve.NEXTVAL, 2, 'Cuve 1', 5000, 0);
INSERT INTO Pompe (id, id_product, name, qte_max, qte_initial) VALUES (seq_cuve.NEXTVAL, 2, 'Cuve 2', 6000, 0);
INSERT INTO Pompe (id, id_product, name, qte_max, qte_initial) VALUES (seq_cuve.NEXTVAL, 2, 'Cuve 3', 7000, 0);


-- Cuve Reference
INSERT INTO CuveReference (id, id_pompe, quantity, mesure) VALUES (seq_cuvereference.NEXTVAL, 3, 5, 1);
INSERT INTO CuveReference (id, id_pompe, quantity, mesure) VALUES (seq_cuvereference.NEXTVAL, 3, 10, 2);
INSERT INTO CuveReference (id, id_pompe, quantity, mesure) VALUES (seq_cuvereference.NEXTVAL, 3, 15, 3);
INSERT INTO CuveReference (id, id_pompe, quantity, mesure) VALUES (seq_cuvereference.NEXTVAL, 3, 20, 7);
INSERT INTO CuveReference (id, id_pompe, quantity, mesure) VALUES (seq_cuvereference.NEXTVAL, 3, 25, 10);
INSERT INTO CuveReference (id, id_pompe, quantity, mesure) VALUES (seq_cuvereference.NEXTVAL, 3, 30, 14);
INSERT INTO CuveReference (id, id_pompe, quantity, mesure) VALUES (seq_cuvereference.NEXTVAL, 3, 35, 71);
INSERT INTO CuveReference (id, id_pompe, quantity, mesure) VALUES (seq_cuvereference.NEXTVAL, 3, 40, 28);
INSERT INTO CuveReference (id, id_pompe, quantity, mesure) VALUES (seq_cuvereference.NEXTVAL, 3, 45, 29);
INSERT INTO CuveReference (id, id_pompe, quantity, mesure) VALUES (seq_cuvereference.NEXTVAL, 3, 50, 30);
INSERT INTO CuveReference (id, id_pompe, quantity, mesure) VALUES (seq_cuvereference.NEXTVAL, 3, 55, 34);
INSERT INTO CuveReference (id, id_pompe, quantity, mesure) VALUES (seq_cuvereference.NEXTVAL, 3, 60, 36);
INSERT INTO CuveReference (id, id_pompe, quantity, mesure) VALUES (seq_cuvereference.NEXTVAL, 3, 95, 41);
INSERT INTO CuveReference (id, id_pompe, quantity, mesure) VALUES (seq_cuvereference.NEXTVAL, 3, 100, 45);
INSERT INTO CuveReference (id, id_pompe, quantity, mesure) VALUES (seq_cuvereference.NEXTVAL, 3, 105, 47);
INSERT INTO CuveReference (id, id_pompe, quantity, mesure) VALUES (seq_cuvereference.NEXTVAL, 3, 110, 52);