--
-- PARITé
--
CREATE OR REPLACE VIEW v_pompiste_parity AS
SELECT 
    id_pompiste,
    CASE 
        WHEN MOD(COUNT(*), 2) = 0 THEN 'TRUE' 
        ELSE 'FALSE' 
    END AS isEven
FROM 
    Prelevement
WHERE 
    id_pompiste IS NOT NULL  -- Filter out NULLs
GROUP BY 
    id_pompiste;

--
-- VENTES POMPES
--
CREATE OR REPLACE VIEW v_pompe_ventes AS 
WITH Sessions AS (
    SELECT
        id,
        id_product,
        id_pompiste,
        id_pompe,
        amount,
        date_prelevement,
        LAG(amount) OVER (PARTITION BY id_pompiste, id_pompe ORDER BY date_prelevement, amount) AS amount_previous,
        LAG(date_prelevement) OVER (PARTITION BY id_pompiste, id_pompe ORDER BY date_prelevement) AS date_previous,
        ROW_NUMBER() OVER (PARTITION BY id_pompiste, id_pompe ORDER BY date_prelevement, amount) AS rn,
        CASE 
            WHEN MOD(ROW_NUMBER() OVER (PARTITION BY id_pompiste, id_pompe ORDER BY date_prelevement), 2) = 0 THEN 'TRUE'
            ELSE 'FALSE'
        END AS isEven
    FROM
        Prelevement
    WHERE id_pompiste IS NOT NULL
)
SELECT 
    id AS id_prelevement,
    id_product,
    id_pompiste,
    id_pompe,
    date_previous AS date_beginning_session,
    date_prelevement AS date_end_session,
    amount_previous AS amount_beginning,
    amount AS amount_end
FROM 
    Sessions
WHERE 
    isEven = 'TRUE';

--
-- VENTES PER POMPES AND PER DATES 
-- 
CREATE OR REPLACE VIEW v_ventes_per_pompe_and_date AS
SELECT
    id_prelevement,
    id_product,
    id_pompe,
    TRUNC(date_end_session) AS date_vente,  
    SUM(amount_end - amount_beginning) AS somme_ventes,
    SUM(amount_end - amount_beginning) / p.PU_vente AS qte_in_liter
FROM 
    v_pompe_ventes
JOIN 
    Product p
ON 
    v_pompe_ventes.id_product = p.id
GROUP BY
    id_product,
    id_pompe,
    TRUNC(date_end_session),
    id_prelevement,
    p.PU_vente
ORDER BY
    id_pompe,
    date_vente;

--
-- MESUREMENT CONSECUTIFS DIFF
--
CREATE OR REPLACE VIEW v_mesurement_diff AS
SELECT
    m.id,
    m.id_pompe,
    m.date_mesurement,
    m.mesure,
    LAG(m.mesure) OVER (PARTITION BY m.id_pompe ORDER BY m.date_mesurement) - m.mesure AS mesure_diff,
    LAG(cr.quantity) OVER (PARTITION BY m.id_pompe ORDER BY m.date_mesurement) - cr.quantity AS diff_in_liter
FROM
    Mesurement m
JOIN
    CuveReference cr
    ON m.id_pompe = cr.id_pompe
    AND m.mesure = cr.mesure;
 
--
-- ANOMALIE
--
CREATE OR REPLACE VIEW v_anomalie AS
WITH closest_mesurement AS (
    SELECT
        vvp.id_pompe,
        vvp.date_vente,
        vvp.qte_in_liter AS prelevement_quantity,
        vmd.date_mesurement,
        vmd.mesure_diff AS mesure_diff_in_cm, -- Difference in cm from mesurement
        vmd.diff_in_liter AS mesurement_quantity,
        cr.quantity / cr.mesure AS liter_to_cm_ratio, -- Conversion ratio: liters per cm
        ROW_NUMBER() OVER (PARTITION BY vvp.id_pompe, vvp.date_vente ORDER BY vmd.date_mesurement DESC) AS rn
    FROM 
        v_ventes_per_pompe_and_date vvp
    LEFT JOIN
        v_mesurement_diff vmd
        ON vvp.id_pompe = vmd.id_pompe
        AND vmd.date_mesurement <= vvp.date_vente  -- only consider measurements on or before the vente date
    LEFT JOIN
        CuveReference cr
        ON vvp.id_pompe = cr.id_pompe -- Join to get reference for conversion
        AND vmd.mesure = cr.mesure
)
SELECT
    cm.id_pompe,
    cm.date_vente,
    cm.prelevement_quantity AS prelev_in_L,
    cm.mesurement_quantity AS mesure_in_L,
    cm.prelevement_quantity / cm.liter_to_cm_ratio AS prelevement_in_cm, -- Prelevement in cm
    cm.mesurement_quantity / cm.liter_to_cm_ratio AS mesure_in_cm, -- Mesurement in cm
    (cm.prelevement_quantity / cm.liter_to_cm_ratio) - (cm.mesurement_quantity / cm.liter_to_cm_ratio) AS anomalie_in_cm, -- Difference in cm
    cm.prelevement_quantity - cm.mesurement_quantity AS anomalie_in_L
FROM
    closest_mesurement cm
WHERE
    cm.rn = 1 -- select only the closest previous measurement for each prelevement
ORDER BY 
    cm.id_pompe,
    cm.date_vente;