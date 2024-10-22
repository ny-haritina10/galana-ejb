package order;

import java.sql.*;
import base.BaseModel;
import database.Database;
import order.Order;
import product.Product;

public class OrderItem extends BaseModel<OrderItem> {

    private int id;
    private Order order;  
    private Product product;
    private int quantity;

    // Constructor
    public OrderItem() {}

    public OrderItem(int id, Order order, Product product, int quantity) {
        setId(id);
        setOrder(order);
        setProduct(product);
        setQuantity(quantity);
    }


    public void delete(Connection connection) throws SQLException {
        PreparedStatement statement = null;

        try {
            if (connection == null) {
                connection = Database.getConnection();
            }

            String sql = "DELETE FROM OrderItems WHERE id = ?";
            statement = connection.prepareStatement(sql);

            // Set the ID of the OrderItem to delete
            statement.setInt(1, this.id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Deleting order item failed, no rows affected.");
            } else {
                System.out.println("OrderItem with ID " + this.id + " deleted successfully.");
            }
        } finally {
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

    public void insert(Connection connection) throws SQLException {
        PreparedStatement statement = null;

        try {
            if (connection == null) {
                connection = Database.getConnection();
            }

            String sql = "INSERT INTO OrderItems (id, id_order, id_product, quantity) VALUES (seq_order_items.NEXTVAL, ?, ?, ?)";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, this.order.getId());
            statement.setInt(2, this.product.getId());
            statement.setInt(3, this.quantity);

            System.out.println("order id from items" + this.order.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Inserting order item failed, no rows affected.");
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        if (order != null) {
            this.order = order;
        } else {
            throw new IllegalArgumentException("Order cannot be null.");
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity > 0) {
            this.quantity = quantity;
        } else {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
    }

    @Override
    public String toString() {
        return "OrderItem{id=" + id + ", order=" + order + ", product=" + product + ", quantity=" + quantity + "}";
    }

    @Override
    protected OrderItem mapRow(ResultSet resultSet) throws Exception {
        int id = resultSet.getInt("id");
        Order order = new Order().getById(resultSet.getInt("id_order"), Order.class, null);
        Product product = new Product().getById(resultSet.getInt("id_product"), Product.class, null);
        int quantity = resultSet.getInt("quantity");

        return new OrderItem(id, order, product, quantity);
    }
}
