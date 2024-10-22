package stock;

import java.sql.*;
import base.BaseModel;

public class StockDetail extends BaseModel<StockDetail> {

    private int stockId;
    private Date dateSession;
    private double quantityIn;
    private double quantityOut;
    private int productId;
    private String productName;
    private double purchasePrice;
    private double salePrice;
    private String typeProduct;
    private double initialQuantity;

    // Constructor
    public StockDetail() {}

    public StockDetail(int stockId, Date dateSession, double quantityIn, double quantityOut, int productId, String productName, double purchasePrice, double salePrice, String typeProduct, double initialQuantity) {
        setStockId(stockId);
        setDateSession(dateSession);
        setQuantityIn(quantityIn);
        setQuantityOut(quantityOut);
        setProductId(productId);
        setProductName(productName);
        setPurchasePrice(purchasePrice);
        setSalePrice(salePrice);
        setTypeProduct(typeProduct);
        setInitialQuantity(initialQuantity);
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        if (stockId > 0) {
            this.stockId = stockId;
        } else {
            throw new IllegalArgumentException("Stock ID must be positive.");
        }
    }

    public Date getDateSession() {
        return dateSession;
    }

    public void setDateSession(Date dateSession) {
        this.dateSession = dateSession;
    }

    public double getQuantityIn() {
        return quantityIn;
    }

    public void setQuantityIn(double quantityIn) {
        if (quantityIn >= 0) {
            this.quantityIn = quantityIn;
        } else {
            throw new IllegalArgumentException("Quantity in must be non-negative.");
        }
    }

    public double getQuantityOut() {
        return quantityOut;
    }

    public void setQuantityOut(double quantityOut) {
        if (quantityOut >= 0) {
            this.quantityOut = quantityOut;
        } else {
            throw new IllegalArgumentException("Quantity out must be non-negative.");
        }
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

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        if (purchasePrice >= 0) {
            this.purchasePrice = purchasePrice;
        } else {
            throw new IllegalArgumentException("Purchase price must be non-negative.");
        }
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        if (salePrice >= 0) {
            this.salePrice = salePrice;
        } else {
            throw new IllegalArgumentException("Sale price must be non-negative.");
        }
    }

    public String getTypeProduct() {
        return typeProduct;
    }

    public void setTypeProduct(String typeProduct) {
        this.typeProduct = typeProduct;
    }

    public double getInitialQuantity() {
        return initialQuantity;
    }

    public void setInitialQuantity(double initialQuantity) {
        this.initialQuantity = initialQuantity;
    }

    @Override
    public String toString() {
        return "StockDetail{stockId=" + stockId + ", dateSession=" + dateSession + ", quantityIn=" + quantityIn + 
               ", quantityOut=" + quantityOut + ", productId=" + productId + ", productName='" + productName + 
               "', purchasePrice=" + purchasePrice + ", salePrice=" + salePrice + ", typeProduct='" + typeProduct + 
               "', initialQuantity=" + initialQuantity + "}";
    }

    @Override
    protected StockDetail mapRow(ResultSet resultSet) throws Exception {
        int stockId = resultSet.getInt("stock_id");
        Date dateSession = resultSet.getDate("date_session");
        double quantityIn = resultSet.getDouble("quantity_in");
        double quantityOut = resultSet.getDouble("quantity_out");
        int productId = resultSet.getInt("product_id");
        String productName = resultSet.getString("product_name");
        double purchasePrice = resultSet.getDouble("purchase_price");
        double salePrice = resultSet.getDouble("sale_price");
        String typeProduct = resultSet.getString("type_product");
        double initialQuantity = resultSet.getDouble("initial_quantity");

        return new StockDetail(stockId, dateSession, quantityIn, quantityOut, productId, productName, purchasePrice, salePrice, typeProduct, initialQuantity);
    }
}
