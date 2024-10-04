package reference;

import java.sql.*;
import base.BaseModel;
import pompe.Pompe;

public class CuveReference extends BaseModel<CuveReference> {

    private int id;
    private Pompe pompe;
    private double quantity;
    private double mesure;

    // Constructor
    public CuveReference() {}

    public CuveReference(int id, Pompe pompe, double quantity, double mesure) {
        setId(id);
        setPompe(pompe);
        setQuantity(quantity);
        setMesure(mesure);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id > 0) {
            this.id = id;
        } else {
            throw new IllegalArgumentException("ID must be positive.");
        }
    }

    public Pompe getPompe() {
        return pompe;
    }

    public void setPompe(Pompe pompe) {
        this.pompe = pompe;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        if (quantity >= 0) {
            this.quantity = quantity;
        } else {
            throw new IllegalArgumentException("Quantity must be non-negative.");
        }
    }

    public double getMesure() {
        return mesure;
    }

    public void setMesure(double mesure) {
        if (mesure >= 0) {
            this.mesure = mesure;
        } else {
            throw new IllegalArgumentException("Mesure must be non-negative.");
        }
    }

    @Override
    public String toString() {
        return "CuveReference{id=" + id + ", pompe=" + pompe + ", quantity=" + quantity + ", mesure=" + mesure + "}";
    }

    @Override
    protected CuveReference mapRow(ResultSet resultSet) throws Exception {
        int id = resultSet.getInt("id");
        Pompe pompe = new Pompe().getById(resultSet.getInt("id_pompe"), Pompe.class, null);
        double quantity = resultSet.getDouble("quantity");
        double mesure = resultSet.getDouble("mesure");

        return new CuveReference(id, pompe, quantity, mesure);
    }
}