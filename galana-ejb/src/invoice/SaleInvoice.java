package invoice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import encaissement.Encaissement;
import mg.cnaps.compta.ComptaSousEcriture;
import base.BaseModel;
import database.Database;
import order.Order;

import client.Client;

import ejb.ClientServiceBean;
import ejb.ClientServiceRemote;
import ejb.EcritureServiceBean;
import ejb.EcritureServiceRemote;

public class SaleInvoice extends BaseModel<SaleInvoice> {

    private int id;
    private Order order;  
    private double totalAmount;
    private String invoiceType;

    @EJB
    private EcritureServiceRemote remote = new EcritureServiceBean();

    @EJB
    private ClientServiceRemote remoteClient = new ClientServiceBean();

    // Constructor
    public SaleInvoice() {}

    public SaleInvoice(int id, Order order, double totalAmount, String invoiceType) {
        setId(id);
        setOrder(order);
        setTotalAmount(totalAmount);
        setInvoiceType(invoiceType);
    }

    public SaleInvoice(int id, Order order, double totalAmount) {
        setId(id);
        setOrder(order);
        setTotalAmount(totalAmount);
    }    

    public void update(Connection connection, boolean isCredit) throws Exception {
        PreparedStatement statement = null;

        try {
            if (connection == null) 
            { connection = Database.getConnection(); }

            String sql = "UPDATE SaleInvoices SET id_order = ?, total_amount = ?, invoice_type = ? WHERE id = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, this.order.getId());
            statement.setDouble(2, this.totalAmount);
            statement.setString(3, isCredit ? "A CREDIT" : "PAYE");
            statement.setInt(4, this.id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) 
            { throw new SQLException("Updating SaleInvoice failed, no rows affected."); }

            // update accounting entries
            List<ComptaSousEcriture> entries;
            if (isCredit) {
                Client client = remoteClient.getClientById(this.getOrder().getIdClient()); 
                entries = Encaissement.makePrevision(client, this.order.getDateOrder(), this.totalAmount);
            } 
            
            else 
            { entries = createInvoiceEntries(); } 
        } 
        
        finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }

    public String getInvoiceType() 
    { return invoiceType; }

    public void setInvoiceType(String invoiceType) 
    { this.invoiceType = invoiceType; }

    public List<ComptaSousEcriture> createInvoiceEntries() 
        throws Exception
    {
        List<ComptaSousEcriture> entries = new ArrayList<>();

        entries.add(new ComptaSousEcriture("4110000000000", 0, this.getTotalAmount(), "Facture du client " + this.getOrder().getIdClient() + " le " + this.getOrder().getDateOrder(), " Facture du client " + this.getOrder().getIdClient() + " le " + this.getOrder().getDateOrder() + this.getOrder().getDateOrder(), null, null, null, "COMP000044", "2024", null, this.getOrder().getDateOrder(), null, null));
        entries.add(new ComptaSousEcriture("5300000000000", this.getTotalAmount(), 0, "  Debit Caisse du " + this.getOrder().getDateOrder(), "  Debit Caisse du " + this.getOrder().getDateOrder(), null, null, null, "COMP000036", "2024", null, this.getOrder().getDateOrder(), null, null));

        return entries;
    }

    public void insert(Connection connection, boolean isCredit) 
        throws Exception 
    {
        PreparedStatement statement = null;

        try {
            if (connection == null) 
            { connection = Database.getConnection(); }

            if (!isCredit) {
                String sql = "INSERT INTO SaleInvoices (id, id_order, total_amount) VALUES (seq_sale_invoices.NEXTVAL , ?, ?)";
                statement = connection.prepareStatement(sql);

                statement.setInt(1, this.order.getId());
                statement.setDouble(2, this.totalAmount);

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Inserting SaleInvoice failed, no rows affected.");
                }

                // write entries
                List<ComptaSousEcriture> entries = createInvoiceEntries();
                ComptaSousEcriture[] entriesArray = entries.toArray(new ComptaSousEcriture[0]);

                remote.writeEntries(entriesArray);
            }

            else {
                String sql = "INSERT INTO SaleInvoices (id, id_order, total_amount, invoice_type) VALUES (seq_sale_invoices.NEXTVAL , ?, ?, ?)";
                statement = connection.prepareStatement(sql);

                statement.setInt(1, this.order.getId());
                statement.setDouble(2, this.totalAmount);
                statement.setString(3, "A CREDIT");

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) 
                { throw new SQLException("Inserting SaleInvoice failed, no rows affected."); }

                // static client for now
                String idClient = this.getOrder().getIdClient();
                Client client = remoteClient.getClientById(idClient);

                // write entries
                List<ComptaSousEcriture> entries = Encaissement.makePrevision(client, this.order.getDateOrder(), this.totalAmount);

                ComptaSousEcriture[] entriesArray = entries.toArray(new ComptaSousEcriture[0]);
                remote.writeEntries(entriesArray);
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

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        if (totalAmount >= 0) {
            this.totalAmount = totalAmount;
        } else {
            throw new IllegalArgumentException("Total amount must be non-negative.");
        }
    }

    @Override
    public String toString() {
        return "SaleInvoice{id=" + id + ", order=" + order + ", totalAmount=" + totalAmount + "}";
    }

    @Override
    protected SaleInvoice mapRow(ResultSet resultSet) 
        throws Exception 
    {
        int id = resultSet.getInt("id");
        Order order = new Order().getById(resultSet.getInt("id_order"), Order.class, null, "ORDERS");
        double totalAmount = resultSet.getDouble("total_amount");
        String invoiceType = resultSet.getString("invoice_type");


        return new SaleInvoice(id, order, totalAmount, invoiceType);
    }
}