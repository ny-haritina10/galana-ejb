package product;

import java.sql.*;
import base.BaseModel;
import unit.Unit;

public class Product extends BaseModel<Product> {

    private int id;
    private Unit unit;
    private String name;
    private double puAchat;
    private double puVente;
    private String typeProduct;
    private double qteInitial;
    private String sousType;

    // Constructor
    public Product() {}

    public Product(int id, Unit unit, String name, double puAchat, double puVente, String typeProduct, double qteInitial, String sousType) {
        setId(id);
        setUnit(unit);
        setName(name);
        setPuAchat(puAchat);
        setPuVente(puVente);
        setTypeProduct(typeProduct);
        setQteInitial(qteInitial);
        setSousType(sousType);
    }

    public double getQteInitial() {
        return qteInitial;
    }

    public void setQteInitial(double qteInitial) {
        this.qteInitial = qteInitial;
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

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty() && name.length() <= 255) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Name cannot be null, empty, or longer than 255 characters.");
        }
    }

    public double getPuAchat() {
        return puAchat;
    }

    public void setPuAchat(double puAchat) {
        if (puAchat >= 0) {
            this.puAchat = puAchat;
        } else {
            throw new IllegalArgumentException("Purchase price must be non-negative.");
        }
    }

    public double getPuVente() {
        return puVente;
    }

    public void setPuVente(double puVente) {
        if (puVente >= 0) {
            this.puVente = puVente;
        } else {
            throw new IllegalArgumentException("Sale price must be non-negative.");
        }
    }

    @Override
    public String toString() {
        return "Product{id=" + id + ", unit=" + unit + ", name='" + name + "', puAchat=" + puAchat + ", puVente=" + puVente + "}";
    }

    @Override
    protected Product mapRow(ResultSet resultSet) throws Exception {
        int id = resultSet.getInt("id");
        Unit unit = new Unit().getById(resultSet.getInt("id_unit"), Unit.class, null);
        String name = resultSet.getString("name");
        double puAchat = resultSet.getDouble("PU_achat");
        double puVente = resultSet.getDouble("PU_vente");
        String typeProduct = resultSet.getString("type_product");
        double qteInitial = resultSet.getDouble("qte_initial");
        String sousType = resultSet.getString("sous_type");

        return new Product(id, unit, name, puAchat, puVente, typeProduct, qteInitial, sousType);
    }

    public String getTypeProduct() {
        return typeProduct;
    }

    public void setTypeProduct(String typeProduct) {
        this.typeProduct = typeProduct;
    }

    public String getSousType() {
        return sousType;
    }

    public void setSousType(String sousType) {
        this.sousType = sousType;
    }
}