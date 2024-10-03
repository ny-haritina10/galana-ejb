package pompe;

import java.sql.*;
import base.BaseModel;
import product.Product;

public class Pompe extends BaseModel<Pompe> {

    private int id;
    private Product product;
    private String name;
    private double qteMax;
    private double qteInitial;

    // Constructor
    public Pompe() {}

    public Pompe(int id, Product product, String name, double qteMax, double qteInitial) {
        setId(id);
        setProduct(product);
        setName(name);
        setQteMax(qteMax);
        setQteInitial(qteInitial);
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    public double getQteMax() {
        return qteMax;
    }

    public void setQteMax(double qteMax) {
        if (qteMax >= 0) {
            this.qteMax = qteMax;
        } else {
            throw new IllegalArgumentException("Max quantity must be non-negative.");
        }
    }

    public double getQteInitial() {
        return qteInitial;
    }

    public void setQteInitial(double qteInitial) {
        if (qteInitial >= 0 && qteInitial <= qteMax) {
            this.qteInitial = qteInitial;
        } else {
            throw new IllegalArgumentException("Initial quantity must be between 0 and max quantity.");
        }
    }

    @Override
    public String toString() {
        return "Pompe{id=" + id + ", product=" + product + ", name='" + name + "', qteMax=" + qteMax + ", qteInitial=" + qteInitial + "}";
    }

    @Override
    protected Pompe mapRow(ResultSet resultSet) throws Exception {
        int id = resultSet.getInt("id");
        Product product = new Product().getById(resultSet.getInt("id_product"), Product.class, null);
        String name = resultSet.getString("name");
        double qteMax = resultSet.getDouble("qte_max");
        double qteInitial = resultSet.getDouble("qte_initial");

        return new Pompe(id, product, name, qteMax, qteInitial);
    }
}