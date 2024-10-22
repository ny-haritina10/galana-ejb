package invoice;

import java.sql.*;
import base.BaseModel;

public class SaleInvoiceDetail extends BaseModel<SaleInvoiceDetail> {

    private int idInvoice;
    private int orderId;    
    private String clientId;
    private Date orderDate;
    private int idProduct;
    private String productName;
    private int productQuantity;
    private double unitPrice;
    private double lineTotal;
    private double totalInvoiceAmount;

    // Constructor
    public SaleInvoiceDetail() {}

    public SaleInvoiceDetail(int idInvoice, int orderId, String clientId, Date orderDate, int idProduct, String productName, int productQuantity, double unitPrice, double lineTotal, double totalInvoiceAmount) {
        setOrderId(orderId);
        setClientId(clientId);
        setOrderDate(orderDate);
        setIdProduct(idProduct);
        setProductName(productName);
        setProductQuantity(productQuantity);
        setUnitPrice(unitPrice);
        setLineTotal(lineTotal);
        setTotalInvoiceAmount(totalInvoiceAmount);
        setIdInvoice(idInvoice);
    }

    

    

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        if (orderId > 0) {
            this.orderId = orderId;
        } else {
            throw new IllegalArgumentException("Order ID must be positive.");
        }
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        if (clientId != null && !clientId.trim().isEmpty()) {
            this.clientId = clientId;
        } else {
            throw new IllegalArgumentException("Client ID cannot be null or empty.");
        }
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
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

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        if (productQuantity > 0) {
            this.productQuantity = productQuantity;
        } else {
            throw new IllegalArgumentException("Product quantity must be positive.");
        }
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        if (unitPrice >= 0) {
            this.unitPrice = unitPrice;
        } else {
            throw new IllegalArgumentException("Unit price must be non-negative.");
        }
    }

    public double getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(double lineTotal) {
        this.lineTotal = lineTotal;
    }

    public double getTotalInvoiceAmount() {
        return totalInvoiceAmount;
    }

    public void setTotalInvoiceAmount(double totalInvoiceAmount) {
        if (totalInvoiceAmount >= 0) {
            this.totalInvoiceAmount = totalInvoiceAmount;
        } else {
            throw new IllegalArgumentException("Total invoice amount must be non-negative.");
        }
    }

    @Override
    public String toString() {
        return "SaleInvoiceDetail{orderId=" + orderId + ", clientId='" + clientId + "', orderDate=" + orderDate +
               ", productName='" + productName + "', productQuantity=" + productQuantity + 
               ", unitPrice=" + unitPrice + ", lineTotal=" + lineTotal +
               ", totalInvoiceAmount=" + totalInvoiceAmount + "}";
    }

    @Override
    protected SaleInvoiceDetail mapRow(ResultSet resultSet) 
        throws Exception 
    {
        int idInvoice = resultSet.getInt("id_invoice");
        int orderId = resultSet.getInt("order_id");
        String clientId = resultSet.getString("client_id");
        Date orderDate = resultSet.getDate("order_date");
        int idProduct = resultSet.getInt("id_product");
        String productName = resultSet.getString("product_name");
        int productQuantity = resultSet.getInt("product_quantity");
        double unitPrice = resultSet.getDouble("unit_price");
        double lineTotal = resultSet.getDouble("line_total");
        double totalInvoiceAmount = resultSet.getDouble("total_invoice_amount");

        return new SaleInvoiceDetail(idInvoice, orderId, clientId, orderDate, idProduct, productName, productQuantity, unitPrice, lineTotal, totalInvoiceAmount);
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getIdInvoice() {
        return idInvoice;
    }

    public void setIdInvoice(int idInvoice) {
        this.idInvoice = idInvoice;
    }
}
