package stock;

import java.sql.*;
import base.BaseModel;
import database.Database;
import product.Product;

public class Stock extends BaseModel<Stock> {

    private int id;
    private Product product;  
    private Date dateSession;
    private int quantityIn;
    private int quantityOut;

    // Constructor
    public Stock() {}

    public Stock(int id, Product product, Date dateSession, int quantityIn, int quantityOut) {
        setId(id);
        setProduct(product);
        setDateSession(dateSession);
        setQuantityIn(quantityIn);
        setQuantityOut(quantityOut);
    }

    public void insert(Connection connection) throws SQLException {
        PreparedStatement statement = null;

        try {
            if (connection == null) {
                connection = Database.getConnection();
            }

            String sql = "INSERT INTO Stock (id, id_product, date_session, quantity_in, quantity_out) VALUES (seq_stock.NEXTVAL, ?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, this.product.getId());
            statement.setDate(2, this.dateSession);
            statement.setInt(3, this.quantityIn);
            statement.setInt(4, this.quantityOut);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Inserting Stock failed, no rows affected.");
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

    public static void reverseStockAdjustment(Product product, int quantity, Date dateSession) 
        throws Exception 
    {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = Database.getConnection();

            String sql = "INSERT INTO Stock (id, id_product, date_session, quantity_in, quantity_out) VALUES (seq_stock.NEXTVAL, ?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, product.getId());
            statement.setDate(2, dateSession);
            statement.setInt(3, quantity); // reverse the quantity out by adding it back as quantity in
            statement.setInt(4, 0);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Reversing stock adjustment failed, no rows affected.");
            }
        } 
        finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static QuantityResult checkAndAdjustQuantity(Product product, int requestedQuantity, Date dateSession) 
        throws Exception 
    {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int availableQuantity = 0;

        try {
            connection = Database.getConnection();

            // get the current stock level, including initial quantity
            String sql = "SELECT (p.qte_initial + COALESCE(SUM(s.quantity_in), 0) - COALESCE(SUM(s.quantity_out), 0)) AS available_quantity " +
                         "FROM Product p " +
                         "LEFT JOIN Stock s ON p.id = s.id_product AND s.date_session <= ? " +
                         "WHERE p.id = ? " +
                         "GROUP BY p.id, p.qte_initial";
            statement = connection.prepareStatement(sql);
            statement.setDate(1, dateSession);
            statement.setInt(2, product.getId());

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                availableQuantity = resultSet.getInt("available_quantity");
            }

            int adjustedQuantity = Math.min(requestedQuantity, availableQuantity);
            int excessQuantity = Math.max(0, requestedQuantity - availableQuantity);

            return new QuantityResult(adjustedQuantity, excessQuantity);
        } 

        catch(Exception e)
        { e.printStackTrace(); }
        
        finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return null;
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
        if (product != null) {
            this.product = product;
        } else {
            throw new IllegalArgumentException("Product cannot be null.");
        }
    }

    public Date getDateSession() {
        return dateSession;
    }

    public void setDateSession(Date dateSession) {
        if (dateSession != null) {
            this.dateSession = dateSession;
        } else {
            throw new IllegalArgumentException("Date session cannot be null.");
        }
    }

    public int getQuantityIn() {
        return quantityIn;
    }

    public void setQuantityIn(int quantityIn) {
        if (quantityIn >= 0) {
            this.quantityIn = quantityIn;
        } else {
            throw new IllegalArgumentException("Quantity in must be non-negative.");
        }
    }

    public int getQuantityOut() {
        return quantityOut;
    }

    public void setQuantityOut(int quantityOut) {
        if (quantityOut >= 0) {
            this.quantityOut = quantityOut;
        } else {
            throw new IllegalArgumentException("Quantity out must be non-negative.");
        }
    }

    @Override
    public String toString() {
        return "Stock{id=" + id + ", product=" + product + ", dateSession=" + dateSession + 
               ", quantityIn=" + quantityIn + ", quantityOut=" + quantityOut + "}";
    }

    @Override
    protected Stock mapRow(ResultSet resultSet) throws Exception {
        int id = resultSet.getInt("id");
        Product product = new Product().getById(resultSet.getInt("id_product"), Product.class, null);
        Date dateSession = resultSet.getDate("date_session");
        int quantityIn = resultSet.getInt("quantity_in");
        int quantityOut = resultSet.getInt("quantity_out");

        return new Stock(id, product, dateSession, quantityIn, quantityOut);
    }

    public static class QuantityResult {
        public int adjustedQuantity;
        public int excessQuantity;

        public QuantityResult(int adjustedQuantity, int excessQuantity) {
            this.adjustedQuantity = adjustedQuantity;
            this.excessQuantity = excessQuantity;
        }
    }
}