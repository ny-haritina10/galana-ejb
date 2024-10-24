package vente;

import java.sql.*;
import java.util.Date;

import base.BaseModel;
import pompe.Pompe;
import product.Product;

public class Vente extends BaseModel<Vente> {

    private Product product;
    private Pompe pompe;
    private Date dateVente;
    private double sommeVentes;

    // Constructor
    public Vente() {}

    public Vente(Product product, Pompe pompe, Date dateVente, double sommeVentes) {
        setProduct(product);
        setPompe(pompe);
        setDateVente(dateVente);
        setSommeVentes(sommeVentes);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Pompe getPompe() {
        return pompe;
    }

    public void setPompe(Pompe pompe) {
        this.pompe = pompe;
    }

    public Date getDateVente() {
        return dateVente;
    }

    public void setDateVente(Date dateVente) {
        if (dateVente != null) {
            this.dateVente = dateVente;
        } else {
            throw new IllegalArgumentException("Sale date cannot be null.");
        }
    }

    public double getSommeVentes() {
        return sommeVentes;
    }

    public void setSommeVentes(double sommeVentes) {
        if (sommeVentes >= 0) {
            this.sommeVentes = sommeVentes;
        } else {
            throw new IllegalArgumentException("Sales amount must be non-negative.");
        }
    }

    @Override
    public String toString() {
        return "Vente{product=" + product + ", pompe=" + pompe + ", dateVente=" + dateVente + 
                ", sommeVentes=" + sommeVentes + "}";
    }

    @Override
    protected Vente mapRow(ResultSet resultSet) throws Exception {
        Product product = new Product().getById(resultSet.getInt("id_product"), Product.class, null);
        Pompe pompe = new Pompe().getById(resultSet.getInt("id_pompe"), Pompe.class, null);
        Date dateVente = resultSet.getDate("date_vente");
        double sommeVentes = resultSet.getDouble("somme_ventes");

        return new Vente(product, pompe, dateVente, sommeVentes);
    }
}