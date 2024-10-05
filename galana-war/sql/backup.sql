-- --
-- -- RESTE QTE 
-- -- 
-- CREATE OR REPLACE VIEW v_qte_stock AS
-- SELECT 
--     m.*,
--     cr.quantity
-- FROM 
--     Mesurement m
-- JOIN 
--     CuveReference cr
--     ON m.id_pompe = cr.id_pompe
--     AND m.mesure = cr.mesure
-- JOIN 
--     (
--         -- Subquery to find the latest date_mesurement for each id_pompe
--         SELECT 
--             id_pompe, 
--             MAX(date_mesurement) AS latest_date_mesurement
--         FROM 
--             Mesurement
--         GROUP BY 
--             id_pompe
--     ) 
-- latest  ON m.id_pompe = latest.id_pompe
--         AND m.date_mesurement = latest.latest_date_mesurement;
/*
    PU_v = 2$

    PRELEV A:
    150 - 100 = 50$
    50$ -> 25L 

    PRELEV B:
    300 - 150 = 150$
    150$ -> 75L

    MESURE A:
    50 - 45 = 5cm
    5cm -> 25L

    MESURE B:
    45 - 35 = 10cm
    10cm -> 50L 

    ** CONCLUSION **
    PRELEV A & MESURE A : 
        => 25 - 25 = 0
        => Pas d'anomalie

    PRELEV B & MESURE B : 
        => 75 - 50 = 25 
        => Anomalie de 25L ou de 5cm
*/