package creance;

import java.sql.*;
import base.BaseModel;
import database.Database;
import prelevement.Prelevement;

public class DetailCreance extends BaseModel<DetailCreance> {

    private int id;
    private Prelevement prelevement;
    private String idClient;
    private Date dateEcheance;
    private double amount;

    // Constructor
    public DetailCreance() {}

    public DetailCreance(int id, Prelevement prelevement, String idClient, Date dateEcheance, double amount) {
        setId(id);
        setPrelevement(prelevement);
        setIdClient(idClient);
        setDateEcheance(dateEcheance);
        setAmount(amount);
    }

    public void insert(Connection connection) throws Exception {
        PreparedStatement statement = null;
    
        try {
            if (connection == null) { 
                connection = Database.getConnection(); 
            }
    
            String sql = "INSERT INTO Creance (id, id_prelevement, id_client, date_echeance, amount) VALUES (seq_creance.NEXTVAL, ?, ?, ?, ?)";
    
            statement = connection.prepareStatement(sql);
    
            // Setting the values from the object properties
            statement.setInt(1, this.prelevement.getId());  
            statement.setString(2, this.idClient);
            statement.setDate(3, new java.sql.Date(this.dateEcheance.getTime()));  
            statement.setDouble(4, this.amount);
    
            int affectedRows = statement.executeUpdate();
    
            if (affectedRows == 0) {
                throw new SQLException("Inserting Creance failed, no rows affected.");
            }
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
        if (id > 0) {
            this.id = id;
        } else {
            throw new IllegalArgumentException("ID must be positive.");
        }
    }

    public Prelevement getPrelevement() {
        return prelevement;
    }

    public void setPrelevement(Prelevement prelevement) {
        this.prelevement = prelevement;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        if (idClient != null && !idClient.isEmpty()) {
            this.idClient = idClient;
        } else {
            throw new IllegalArgumentException("Client ID cannot be null or empty.");
        }
    }

    public Date getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(Date dateEcheance) {
        if (dateEcheance != null) {
            this.dateEcheance = dateEcheance;
        } else {
            throw new IllegalArgumentException("Date Echeance cannot be null.");
        }
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        if (amount > 0) {
            this.amount = amount;
        } else {
            throw new IllegalArgumentException("Amount must be positive.");
        }
    }

    @Override
    public String toString() {
        return "DetailCreance{id=" + id + ", prelevement=" + prelevement + ", idClient='" + idClient + "', dateEcheance=" + dateEcheance + ", amount=" + amount + "}";
    }

    @Override
    protected DetailCreance mapRow(ResultSet resultSet) 
        throws Exception 
    {
        int id = resultSet.getInt("id");
        Prelevement prelevement = new Prelevement().getById(resultSet.getInt("id_prelevement"), Prelevement.class, null);
        String idClient = resultSet.getString("id_client");
        Date dateEcheance = resultSet.getDate("date_echeance");
        double amount = resultSet.getDouble("amount");

        return new DetailCreance(id, prelevement, idClient, dateEcheance, amount);
    }
}
