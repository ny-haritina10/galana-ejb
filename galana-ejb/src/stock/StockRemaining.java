package stock;

import java.sql.*;
import base.BaseModel;

public class StockRemaining extends BaseModel<StockRemaining> {

    private int productId;
    private String productName;
    private double initialQuantity;
    private double totalQuantityIn;
    private double totalQuantityOut;
    private double remainingQuantity;

    // Constructor
    public StockRemaining() {}

    public StockRemaining(int productId, String productName, double initialQuantity, double totalQuantityIn, double totalQuantityOut, double remainingQuantity) {
        setProductId(productId);
        setProductName(productName);
        setInitialQuantity(initialQuantity);
        setTotalQuantityIn(totalQuantityIn);
        setTotalQuantityOut(totalQuantityOut);
        setRemainingQuantity(remainingQuantity);
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        if (productId > 0) {
            this.productId = productId;
        } else {
            throw new IllegalArgumentException("Product ID must be positive.");
        }
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        if (productName != null && !productName.trim().isEmpty() && productName.length() <= 255) {
            this.productName = productName;
        } else {
            throw new IllegalArgumentException("Product name cannot be null, empty, or longer than 255 characters.");
        }
    }

    public double getInitialQuantity() {
        return initialQuantity;
    }

    public void setInitialQuantity(double initialQuantity) {
        if (initialQuantity >= 0) {
            this.initialQuantity = initialQuantity;
        } else {
            throw new IllegalArgumentException("Initial quantity must be non-negative.");
        }
    }

    public double getTotalQuantityIn() {
        return totalQuantityIn;
    }

    public void setTotalQuantityIn(double totalQuantityIn) {
        if (totalQuantityIn >= 0) {
            this.totalQuantityIn = totalQuantityIn;
        } else {
            throw new IllegalArgumentException("Total quantity in must be non-negative.");
        }
    }

    public double getTotalQuantityOut() {
        return totalQuantityOut;
    }

    public void setTotalQuantityOut(double totalQuantityOut) {
        if (totalQuantityOut >= 0) {
            this.totalQuantityOut = totalQuantityOut;
        } else {
            throw new IllegalArgumentException("Total quantity out must be non-negative.");
        }
    }

    public double getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(double remainingQuantity) {
        if (remainingQuantity >= 0) {
            this.remainingQuantity = remainingQuantity;
        } else {
            throw new IllegalArgumentException("Remaining quantity must be non-negative.");
        }
    }

    @Override
    public String toString() {
        return "StockRemaining{productId=" + productId + ", productName='" + productName + 
               "', initialQuantity=" + initialQuantity + ", totalQuantityIn=" + totalQuantityIn + 
               ", totalQuantityOut=" + totalQuantityOut + ", remainingQuantity=" + remainingQuantity + "}";
    }

    @Override
    protected StockRemaining mapRow(ResultSet resultSet) throws Exception {
        int productId = resultSet.getInt("product_id");
        String productName = resultSet.getString("product_name");
        double initialQuantity = resultSet.getDouble("initial_quantity");
        double totalQuantityIn = resultSet.getDouble("total_quantity_in");
        double totalQuantityOut = resultSet.getDouble("total_quantity_out");
        double remainingQuantity = resultSet.getDouble("remaining_quantity");

        return new StockRemaining(productId, productName, initialQuantity, totalQuantityIn, totalQuantityOut, remainingQuantity);
    }
}