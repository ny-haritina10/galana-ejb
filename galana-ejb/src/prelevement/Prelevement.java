package prelevement;

import java.sql.*;
import java.util.Date;
import base.BaseModel;
import database.Database;
import pompiste.Pompiste;
import pompe.Pompe;
import product.Product;

public class Prelevement extends BaseModel<Prelevement> {

    private int id;
    private Pompiste pompiste;
    private Pompe pompe;
    private Product product;
    private double amount;
    private Date datePrelevement;

    // Constructor
    public Prelevement() {}

    public Prelevement(int id, Pompiste pompiste, Pompe pompe, Product product, double amount, Date datePrelevement) {
        setId(id);
        setPompiste(pompiste);
        setPompe(pompe);
        setProduct(product);
        setAmount(amount);
        setDatePrelevement(datePrelevement);
    }

    public double getPrelevementDifference() {
        String sql = "SELECT somme_ventes FROM v_ventes_per_pompe_and_date WHERE id_prelevement = ?";
        double sommeVentes = 0.0; 
    
        try (Connection connection = Database.getConnection(); 
             PreparedStatement statement = connection.prepareStatement(sql)) {
    
            statement.setInt(1, this.getId());
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) 
            { sommeVentes = resultSet.getDouble("somme_ventes"); }
        } 
        
        catch (SQLException e) 
        { e.printStackTrace(); }
    
        return sommeVentes;
    }    

    public void insert(Connection connection) 
        throws SQLException 
    {
        PreparedStatement statement = null;
    
        try {
            if (connection == null) {
                connection = Database.getConnection();
            }
    
            String sql = "INSERT INTO Prelevement (id, id_pompiste, id_pompe, id_product, amount, date_prelevement) VALUES (seq_prelevement.NEXTVAL, ?, ?, ?, ?, ?)";

            statement = connection.prepareStatement(sql);
    
            statement.setInt(1, this.pompiste.getId());
            statement.setInt(2, this.pompe.getId());
            statement.setInt(3, this.product.getId());
            statement.setDouble(4, this.amount * this.product.getPuVente());
            statement.setDate(5, new java.sql.Date(this.datePrelevement.getTime()));
    
            int affectedRows = statement.executeUpdate();
    
            if (affectedRows == 0) {
                throw new SQLException("Inserting Prelevement failed, no rows affected.");
            }

            System.out.println("----PRELEV INSERTED------");
        } 
        
        finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
    
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id >= 0) {
            this.id = id;
        } else {
            throw new IllegalArgumentException("ID must be positive.");
        }
    }

    public Pompiste getPompiste() {
        return pompiste;
    }

    public void setPompiste(Pompiste pompiste) {
        this.pompiste = pompiste;
    }

    public Pompe getPompe() {
        return pompe;
    }

    public void setPompe(Pompe pompe) {
        this.pompe = pompe;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        if (amount >= 0) {
            this.amount = amount;
        } else {
            throw new IllegalArgumentException("Amount must be non-negative.");
        }
    }

    public Date getDatePrelevement() {
        return datePrelevement;
    }

    public void setDatePrelevement(Date datePrelevement) {
        if (datePrelevement != null) {
            this.datePrelevement = datePrelevement;
        } else {
            throw new IllegalArgumentException("Prelevement date cannot be null.");
        }
    }

    @Override
    public String toString() {
        return "Prelevement{id=" + id + ", pompiste=" + pompiste + ", pompe=" + pompe + ", product=" + product + 
                ", amount=" + amount + ", datePrelevement=" + datePrelevement + "}";
    }

    @Override
    protected Prelevement mapRow(ResultSet resultSet) throws Exception {
        int id = resultSet.getInt("id");
        Pompiste pompiste = new Pompiste().getById(resultSet.getInt("id_pompiste"), Pompiste.class, null);
        Pompe pompe = new Pompe().getById(resultSet.getInt("id_pompe"), Pompe.class, null);
        Product product = new Product().getById(resultSet.getInt("id_product"), Product.class, null);
        double amount = resultSet.getDouble("amount");
        Date datePrelevement = resultSet.getDate("date_prelevement");

        return new Prelevement(id, pompiste, pompe, product, amount, datePrelevement);
    }
}