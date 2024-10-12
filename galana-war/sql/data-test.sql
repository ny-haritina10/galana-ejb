-- UNIT
INSERT INTO Unit (id, name) VALUES (seq_unit.NEXTVAL, 'Liter');

-- POMPISTE
INSERT INTO Pompiste (id, name) VALUES (seq_pompiste.NEXTVAL, 'John Doe');
INSERT INTO Pompiste (id, name) VALUES (seq_pompiste.NEXTVAL, 'Jane Smith');

-- PRODUCT
INSERT INTO Product (id, id_unit, name, PU_achat, PU_vente) VALUES (seq_product.NEXTVAL, 2, 'Essence', 1.10, 2);

-- POMPE
INSERT INTO Pompe (id, id_product, name, qte_max, qte_initial) VALUES (seq_cuve.NEXTVAL, 2, 'Cuve 1', 5000, 0);
INSERT INTO Pompe (id, id_product, name, qte_max, qte_initial) VALUES (seq_cuve.NEXTVAL, 2, 'Cuve 2', 6000, 0);
INSERT INTO Pompe (id, id_product, name, qte_max, qte_initial) VALUES (seq_cuve.NEXTVAL, 2, 'Cuve 3', 7000, 0);

-- Prelevement
INSERT INTO Prelevement (id, id_pompiste, id_pompe, id_product, amount, date_prelevement)
VALUES (seq_prelevement.NEXTVAL, 2, 3, 2, 100, TO_DATE('2024-10-03', 'YYYY-MM-DD'));
INSERT INTO Prelevement (id, id_pompiste, id_pompe, id_product, amount, date_prelevement)
VALUES (seq_prelevement.NEXTVAL, 2, 3, 2, 150, TO_DATE('2024-10-04', 'YYYY-MM-DD'));

INSERT INTO Prelevement (id, id_pompiste, id_pompe, id_product, amount, date_prelevement)
VALUES (seq_prelevement.NEXTVAL, 2, 3, 2, 150, TO_DATE('2024-10-05', 'YYYY-MM-DD'));
INSERT INTO Prelevement (id, id_pompiste, id_pompe, id_product, amount, date_prelevement)
VALUES (seq_prelevement.NEXTVAL, 2, 3, 2, 300, TO_DATE('2024-10-06', 'YYYY-MM-DD'));


-- Mesurement
INSERT INTO Mesurement (id, id_pompe, date_mesurement, mesure) 
VALUES (seq_mesurement.NEXTVAL, 3, TO_DATE('2024-10-03', 'YYYY-MM-DD'), 50);

INSERT INTO Mesurement (id, id_pompe, date_mesurement, mesure) 
VALUES (seq_mesurement.NEXTVAL, 3, TO_DATE('2024-10-04', 'YYYY-MM-DD'), 45);

INSERT INTO Mesurement (id, id_pompe, date_mesurement, mesure) 
VALUES (seq_mesurement.NEXTVAL, 3, TO_DATE('2024-10-05', 'YYYY-MM-DD'), 35); -- EXPECTED = 30


-- Cuve Reference
-- DATA IZY 
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