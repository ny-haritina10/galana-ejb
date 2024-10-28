--
-- INTERPOLATION FOR MESURE
--
CREATE OR REPLACE FUNCTION interpolate_cm_to_liter(
    p_pompe_id IN INT,
    p_mesure_cm IN NUMBER
) RETURN NUMBER IS
    v_quantity NUMBER;
    v_lower_mesure NUMBER;
    v_lower_quantity NUMBER;
    v_upper_mesure NUMBER;
    v_upper_quantity NUMBER;
BEGIN
    -- Find the lower and upper reference points
    SELECT mesure, quantity
    INTO v_lower_mesure, v_lower_quantity
    FROM (
        SELECT mesure, quantity
        FROM CuveReference
        WHERE id_pompe = p_pompe_id
          AND mesure <= p_mesure_cm
        ORDER BY mesure DESC
    ) WHERE ROWNUM = 1;

    SELECT mesure, quantity
    INTO v_upper_mesure, v_upper_quantity
    FROM (
        SELECT mesure, quantity
        FROM CuveReference
        WHERE id_pompe = p_pompe_id
          AND mesure > p_mesure_cm
        ORDER BY mesure ASC
    ) WHERE ROWNUM = 1;

    IF v_lower_mesure IS NULL OR v_upper_mesure IS NULL THEN
        SELECT quantity
        INTO v_quantity
        FROM (
            SELECT quantity
            FROM CuveReference
            WHERE id_pompe = p_pompe_id
            ORDER BY ABS(mesure - p_mesure_cm)
        ) WHERE ROWNUM = 1;
    ELSE
        v_quantity := v_lower_quantity + 
            (p_mesure_cm - v_lower_mesure) * 
            (v_upper_quantity - v_lower_quantity) / 
            (v_upper_mesure - v_lower_mesure);
    END IF;

    RETURN v_quantity;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN NULL;
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error in interpolate_cm_to_liter: ' || SQLERRM);
        RAISE;
END interpolate_cm_to_liter;
/