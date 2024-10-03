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
    SUM(amount_end - amount_beginning) AS somme_ventes
FROM 
    v_pompe_ventes
GROUP BY
    id_product,
    id_pompe,
    TRUNC(date_end_session),
    id_prelevement
ORDER BY
    id_pompe,
    date_vente;

--
-- AMOUNT PER POMPES
--
CREATE OR REPLACE VIEW v_vente_totale_per_pompe AS
SELECT
    id_prelevement,
    id_product,
    id_pompe,
    SUM(amount_end - amount_beginning) AS somme_totale
FROM 
    v_pompe_ventes
GROUP BY
    id_product,
    id_pompe,
    id_prelevement
ORDER BY
    id_pompe;