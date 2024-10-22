package order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import base.BaseModel;
import database.Database;
import product.Product;

public class Order extends BaseModel<Order> {

    private int id;
    private String idClient;
    private Date dateOrder;

    // Constructor
    public Order() {}

    public Order(int id, String idClient, Date dateOrder) {
        setId(id);
        setIdClient(idClient);
        setDateOrder(dateOrder);
    }

    public void update(Connection connection) throws Exception {
        PreparedStatement statement = null;

        try {
            if (connection == null) {
                connection = Database.getConnection();
            }

            String sql = "UPDATE Orders SET id_client = ?, date_order = ? WHERE id = ?";
            statement = connection.prepareStatement(sql);

            statement.setString(1, this.idClient);
            statement.setDate(2, this.dateOrder);
            statement.setInt(3, this.id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new Exception("Updating order failed, no rows affected.");
            }
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    // retrieve all OrderItems for this order
    public List<OrderItem> getOrderItems(Connection connection) {
        List<OrderItem> orderItems = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
    
        try {
            if (connection == null) {
                connection = Database.getConnection();
            }
    
            String sql = "SELECT oi.id, oi.id_product, oi.quantity FROM OrderItems oi WHERE oi.id_order = ?";
            
            statement = connection.prepareStatement(sql);
            statement.setInt(1, this.id); 
            
            resultSet = statement.executeQuery();
    
    
            while (resultSet.next()) {
                int orderItemId = resultSet.getInt("id");
                int productId = resultSet.getInt("id_product");
                int quantity = resultSet.getInt("quantity");
    
                Product product = new Product().getById(productId, Product.class, null);
                OrderItem orderItem = new OrderItem(orderItemId, this, product, quantity);
    
                orderItems.add(orderItem);
            }
        } 
    
        catch (Exception e) {
            e.printStackTrace();
        } 
        finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    
        System.out.println("Returned order items: " + orderItems.size());
        return orderItems;
    }    


    // get total amount for an order
    public double getSumAmount(Connection connection) {
        double totalAmount = 0.0;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            if (connection == null)
            { connection = Database.getConnection(); }

            String sql = "SELECT total_amount FROM v_boutique_order_totals WHERE order_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, this.id); 

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                totalAmount = resultSet.getDouble("total_amount");
            }
        } 
        
        catch (Exception e) {
            e.printStackTrace();
        } 
        
        finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return totalAmount;
    }

    public static int getMaxId() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int maxId = 0;
    
        try {
            connection = Database.getConnection();
    
            String sql = "SELECT MAX(id) FROM Orders";
            statement = connection.prepareStatement(sql);
    
            resultSet = statement.executeQuery();
    
            if (resultSet.next()) {
                maxId = resultSet.getInt(1);  
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        } 
        finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
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
    
        return maxId;
    }    

    // insert
    public void insert(Connection connection) throws Exception {
        PreparedStatement statement = null;

        try {
            if (connection == null) {
                connection = Database.getConnection();
            }

            String sql = "INSERT INTO Orders (id, id_client, date_order) VALUES (seq_order.NEXTVAL, ?, ?)";
            statement = connection.prepareStatement(sql);

            statement.setString(1, this.idClient);
            statement.setDate(2, this.dateOrder);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new Exception("Inserting order failed, no rows affected.");
            }
        } 
        
        finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
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

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        if (idClient != null && !idClient.trim().isEmpty() && idClient.length() <= 255) {
            this.idClient = idClient;
        } else {
            throw new IllegalArgumentException("Client ID cannot be null, empty, or longer than 255 characters.");
        }
    }

    public Date getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(Date dateOrder) {
        if (dateOrder != null) {
            this.dateOrder = dateOrder;
        } else {
            throw new IllegalArgumentException("Order date cannot be null.");
        }
    }

    @Override
    public String toString() {
        return "Order{id=" + id + ", idClient='" + idClient + "', dateOrder=" + dateOrder + "}";
    }

    @Override
    protected Order mapRow(ResultSet resultSet) 
        throws Exception 
    {
        int id = resultSet.getInt("id");
        String idClient = resultSet.getString("id_client");
        Date dateOrder = resultSet.getDate("date_order");

        return new Order(id, idClient, dateOrder);
    }
}
