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

/*
    how to handle this situation if the mesure given is not present in the CuveReference Table ? Assuming the Mesure in the table CuveReference is linear ... 
    For example, mesure is 3.4 .. how to find the quantity ?   
*/


-- LINEAR
CREATE OR REPLACE VIEW v_anomalie AS
WITH closest_mesurement AS (
    SELECT
        vvp.id_pompe,
        vvp.date_vente,
        vvp.qte_in_liter AS prelevement_quantity,
        vmd.date_mesurement,
        vmd.mesure_diff AS mesure_diff_in_cm,
        vmd.diff_in_liter AS mesurement_quantity,
        vmd.mesure AS actual_mesure,
        ROW_NUMBER() OVER (PARTITION BY vvp.id_pompe, vvp.date_vente ORDER BY vmd.date_mesurement DESC) AS rn
    FROM 
        v_ventes_per_pompe_and_date vvp
    LEFT JOIN
        v_mesurement_diff vmd
        ON vvp.id_pompe = vmd.id_pompe
        AND vmd.date_mesurement <= vvp.date_vente
),
interpolated_reference AS (
    SELECT
        cm.id_pompe,
        cm.actual_mesure,
        cr_lower.mesure AS lower_mesure,
        cr_lower.quantity AS lower_quantity,
        cr_upper.mesure AS upper_mesure,
        cr_upper.quantity AS upper_quantity,
        cr_lower.quantity + (cm.actual_mesure - cr_lower.mesure) * 
            (cr_upper.quantity - cr_lower.quantity) / (cr_upper.mesure - cr_lower.mesure) AS interpolated_quantity,
        (cr_upper.quantity - cr_lower.quantity) / (cr_upper.mesure - cr_lower.mesure) AS liter_to_cm_ratio
    FROM
        closest_mesurement cm
    JOIN
        CuveReference cr_lower
        ON cm.id_pompe = cr_lower.id_pompe
        AND cr_lower.mesure <= cm.actual_mesure
    JOIN
        CuveReference cr_upper
        ON cm.id_pompe = cr_upper.id_pompe
        AND cr_upper.mesure > cm.actual_mesure
    WHERE
        cm.rn = 1
)
SELECT
    cm.id_pompe,
    cm.date_vente,
    cm.prelevement_quantity AS prelev_in_L,
    cm.mesurement_quantity AS mesure_in_L,
    cm.prelevement_quantity / ir.liter_to_cm_ratio AS prelevement_in_cm,
    cm.mesurement_quantity / ir.liter_to_cm_ratio AS mesure_in_cm,
    (cm.prelevement_quantity / ir.liter_to_cm_ratio) - (cm.mesurement_quantity / ir.liter_to_cm_ratio) AS anomalie_in_cm,
    cm.prelevement_quantity - cm.mesurement_quantity AS anomalie_in_L,
    ir.interpolated_quantity,
    ir.liter_to_cm_ratio
FROM
    closest_mesurement cm
JOIN
    interpolated_reference ir
    ON cm.id_pompe = ir.id_pompe
    AND cm.actual_mesure = ir.actual_mesure
WHERE
    cm.rn = 1
ORDER BY 
    cm.id_pompe,
    cm.date_vente;



-- NON LINEAR 
-- Example of a non-linear or discontinued CuveReference table
CREATE TABLE CuveReference (
    id INT PRIMARY KEY,
    id_pompe INT,
    quantity NUMBER NOT NULL,
    mesure NUMBER NOT NULL
);

-- Sample data demonstrating non-linear relationship
INSERT INTO CuveReference (id, id_pompe, quantity, mesure) VALUES
(1, 1, 100, 1.0),
(2, 1, 200, 2.0),
(3, 1, 400, 3.0),  -- Non-linear jump
(4, 1, 450, 4.0),
(5, 1, 460, 5.0),  -- Plateau
(6, 1, 470, 6.0),
(7, 1, 600, 7.0),  -- Another jump
(8, 1, 610, 8.0);

-- Function to handle non-linear interpolation
CREATE OR REPLACE FUNCTION get_interpolated_quantity(
    p_id_pompe INT,
    p_mesure NUMBER
) RETURNS NUMBER AS $$
DECLARE
    v_lower_mesure NUMBER;
    v_lower_quantity NUMBER;
    v_upper_mesure NUMBER;
    v_upper_quantity NUMBER;
    v_interpolated_quantity NUMBER;
BEGIN
    -- Get the closest lower and upper reference points
    SELECT mesure, quantity
    INTO v_lower_mesure, v_lower_quantity
    FROM CuveReference
    WHERE id_pompe = p_id_pompe AND mesure <= p_mesure
    ORDER BY mesure DESC
    LIMIT 1;

    SELECT mesure, quantity
    INTO v_upper_mesure, v_upper_quantity
    FROM CuveReference
    WHERE id_pompe = p_id_pompe AND mesure > p_mesure
    ORDER BY mesure ASC
    LIMIT 1;

    -- Handle edge cases
    IF v_lower_mesure IS NULL THEN
        RETURN v_upper_quantity;
    ELSIF v_upper_mesure IS NULL THEN
        RETURN v_lower_quantity;
    END IF;

    -- Check for discontinuity or non-linearity
    IF (v_upper_quantity - v_lower_quantity) / (v_upper_mesure - v_lower_mesure) > 100 THEN
        -- Large jump detected, use nearest neighbor
        IF (p_mesure - v_lower_mesure) < (v_upper_mesure - p_mesure) THEN
            RETURN v_lower_quantity;
        ELSE
            RETURN v_upper_quantity;
        END IF;
    ELSIF v_upper_quantity = v_lower_quantity THEN
        -- Plateau detected, return either quantity
        RETURN v_lower_quantity;
    ELSE
        -- Perform linear interpolation
        v_interpolated_quantity := v_lower_quantity + (p_mesure - v_lower_mesure) * 
            (v_upper_quantity - v_lower_quantity) / (v_upper_mesure - v_lower_mesure);
        RETURN v_interpolated_quantity;
    END IF;
END;
$$ LANGUAGE plpgsql;




-- Example usage
SELECT 
    id_pompe,
    mesure AS reference_mesure,
    quantity AS reference_quantity,
    get_interpolated_quantity(id_pompe, mesure + 0.5) AS interpolated_quantity_mid,
    get_interpolated_quantity(id_pompe, mesure + 0.9) AS interpolated_quantity_near_next
FROM CuveReference
WHERE id_pompe = 1
ORDER BY mesure;




-- DETAILS NY TIERS MANANA AVOIR
-- FANTARINA NY DETAILS AN LEH AVOIR
-- ASINA DATE ECHEANCE
    -- SYSYTEME PREVISION FINANCIERE NO MAMOKA NY DATE ECHEANCE 

-- Insert the given data points
INSERT INTO CuveReference (id, id_pompe, quantity, mesure) VALUES
(1, 5, 10, 1),    -- 1 cm = 10 L
(2, 5, 1000, 100);  -- 100 cm = 1000 L

-- Function to interpolate quantity
-- Function to interpolate quantity for Oracle


/*
how to handle the situation where the relationship between measure and quantity is discontinued or non-linear ?
For example let's say 1cm is 10L , 100cm is 1000L , how to find the value in liter of for example, 7.5 cm ? normally 7.5cm is 750L ? How to find that   
*/ 