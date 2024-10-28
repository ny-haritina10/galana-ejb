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
        LAG(amount) OVER (PARTITION BY id_pompiste, id_pompe ORDER BY date_prelevement, id) AS amount_previous,
        LAG(date_prelevement) OVER (PARTITION BY id_pompiste, id_pompe ORDER BY date_prelevement) AS date_previous,
        ROW_NUMBER() OVER (PARTITION BY id_pompiste, id_pompe ORDER BY date_prelevement, id) AS rn,
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
    ABS(SUM(COALESCE(amount_end - amount_beginning, 0))) AS somme_ventes,
    ABS(SUM(COALESCE(amount_end - amount_beginning, 0)) / p.PU_vente) AS qte_in_liter
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



/*============================================= */
/*ANOMALIE ==================================== */
/*============================================= */


--
-- MESUREMENT CONSECUTIFS DIFF
--
CREATE OR REPLACE VIEW v_mesurement_diff AS
SELECT
    m.id,
    m.id_pompe,
    m.date_mesurement,
    m.mesure,
    interpolate_cm_to_liter(m.id_pompe, LAG(m.mesure) OVER (PARTITION BY m.id_pompe ORDER BY m.date_mesurement)) - interpolate_cm_to_liter(m.id_pompe, m.mesure) AS mesure_diff
FROM
    Mesurement m;

--
-- ANOMALIE
--
CREATE OR REPLACE VIEW v_anomalie AS
WITH closest_mesurement AS (
    SELECT
        vvp.id_pompe,
        vvp.date_vente,
        vvp.somme_ventes AS prelevement_quantity,
        vmd.date_mesurement,
        vmd.mesure_diff AS mesure_diff_in_cm, 
        cr.quantity / cr.mesure AS liter_to_cm_ratio, 
        ROW_NUMBER() OVER (PARTITION BY vvp.id_pompe, vvp.date_vente ORDER BY vmd.date_mesurement DESC) AS rn
    FROM 
        v_ventes_per_pompe_and_date vvp
    LEFT JOIN
        v_mesurement_diff vmd
        ON vvp.id_pompe = vmd.id_pompe
        AND vmd.date_mesurement <= vvp.date_vente  
    LEFT JOIN
        CuveReference cr
        ON vvp.id_pompe = cr.id_pompe 
        AND vmd.mesure = cr.mesure
)
SELECT
    cm.id_pompe,
    cm.date_vente,
    cm.prelevement_quantity AS prelev_in_L,
    cm.prelevement_quantity / cm.liter_to_cm_ratio AS prelevement_in_cm, 
    cm.mesure_diff_in_cm  AS mesure_in_l, 
    cm.prelevement_quantity - cm.mesure_diff_in_cm AS anomalie_in_L
FROM
    closest_mesurement cm
WHERE
    cm.rn = 1
ORDER BY 
    cm.id_pompe,
    cm.date_vente;


/*============================================= */
/*BOUTIQUE ==================================== */
/*============================================= */


--
-- SUM AMOUNT OF AN ORDER
-- 
CREATE OR REPLACE VIEW v_boutique_order_totals AS
SELECT 
    o.id AS order_id,
    SUM(oi.quantity * p.PU_vente) AS total_amount
FROM 
    Orders o
JOIN 
    OrderItems oi ON o.id = oi.id_order
JOIN 
    Product p ON oi.id_product = p.id
WHERE 
    p.type_product = 'BOUTIQUE'
GROUP BY 
    o.id;

--
-- STOCK LEVEL
--
CREATE OR REPLACE VIEW v_stock_levels AS
SELECT 
    P.id AS product_id,
    P.name AS product_name,
    P.qte_initial,
    S.date_session,
    COALESCE(SUM(S.quantity_in), 0) AS total_quantity_in,
    COALESCE(SUM(S.quantity_out), 0) AS total_quantity_out,
    COALESCE(SUM(OI.quantity), 0) AS total_ordered,
    (P.qte_initial + COALESCE(SUM(S.quantity_in), 0) 
     - COALESCE(SUM(S.quantity_out), 0)) AS current_stock
FROM 
    Product P
LEFT JOIN Stock S 
    ON P.id = S.id_product 
LEFT JOIN OrderItems OI 
    ON P.id = OI.id_product 
GROUP BY 
    P.id, P.name, P.qte_initial, S.date_session;

--
-- Invoice Detail
--
CREATE OR REPLACE VIEW v_invoice_detail AS
SELECT 
    SI.id AS id_invoice,
    O.id AS order_id,
    O.id_client AS client_id,
    O.date_order AS order_date,
    p.id AS id_product,
    P.name AS product_name,
    OI.quantity AS product_quantity,
    P.PU_vente AS unit_price,
    (OI.quantity * P.PU_vente) AS line_total,
    SI.total_amount AS total_invoice_amount
FROM 
    Orders O
JOIN 
    OrderItems OI ON O.id = OI.id_order
JOIN 
    Product P ON OI.id_product = P.id
JOIN 
    SaleInvoices SI ON O.id = SI.id_order;

--
-- DETAIL STOCK
--
CREATE OR REPLACE VIEW v_detail_stock AS
SELECT
    S.id AS stock_id,
    S.date_session,
    S.quantity_in,
    S.quantity_out,
    P.id AS product_id,
    P.name AS product_name,
    P.PU_achat AS purchase_price,
    P.PU_vente AS sale_price,
    P.type_product,
    P.qte_initial AS initial_quantity
FROM
    Stock S
JOIN
    Product P ON S.id_product = P.id;
    
--
-- RESTE STOCK
--

CREATE OR REPLACE VIEW v_stock_remaining AS
SELECT 
    p.id AS product_id,
    p.name AS product_name,
    p.qte_initial AS initial_quantity,
    COALESCE(SUM(s.quantity_in), 0) AS total_quantity_in,
    COALESCE(SUM(s.quantity_out), 0) AS total_quantity_out,
    (p.qte_initial + COALESCE(SUM(s.quantity_in), 0) - COALESCE(SUM(s.quantity_out), 0)) AS remaining_quantity
FROM 
    Product p
LEFT JOIN 
    Stock s ON p.id = s.id_product
WHERE 
    p.type_product = 'BOUTIQUE' OR p.type_product = 'MIXTE' 

GROUP BY 
    p.id, p.name, p.qte_initial;