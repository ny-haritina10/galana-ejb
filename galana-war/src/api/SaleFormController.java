package api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import client.Client;
import ejb.ClientServiceBean;
import ejb.ClientServiceRemote;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import product.Product;
import order.Order;
import order.OrderItem;
import stock.Stock;
import invoice.SaleInvoice;

import javax.servlet.ServletException;   
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;   
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;   
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.List;

import javax.ejb.EJB;

@WebServlet("/api/sale_form")
public class SaleFormController extends HttpServlet {
    
    private Gson gson = new Gson();

    @EJB
    private ClientServiceRemote remote = new ClientServiceBean();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException 
    {
        try {
            Product[] products = new Product().getAll(Product.class, null);
            List<Client> clients = remote.getAllClients();

            // Combine products and clients in a single JSON response
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.add("products", gson.toJsonTree(products));
            jsonResponse.add("clients", gson.toJsonTree(clients));

            // Set response headers and write the JSON response
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");

            response.getWriter().write(gson.toJson(jsonResponse));
        }
         
        catch (Exception e) {
            e.printStackTrace();

            // Send error response to client
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"An error occurred while fetching data.\"}");
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException 
    {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) 
        { jsonBuilder.append(line); }

        String jsonString = jsonBuilder.toString();

        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);

        try {
            Date saleDate = Date.valueOf(jsonObject.get("saleDate").getAsString());
            String selectedClientId = jsonObject.get("selectedClientId").getAsString();
            JsonArray cartArray = jsonObject.getAsJsonArray("cart");
            boolean isCredit = jsonObject.has("isCredit") && jsonObject.get("isCredit").getAsBoolean();
            StringBuilder message = new StringBuilder();
        
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
        
            // update
            if (jsonObject.has("invoiceId")) {
                int invoiceId = jsonObject.get("invoiceId").getAsInt();
                updateInvoice(invoiceId, selectedClientId, saleDate, cartArray, isCredit);
                response.getWriter().write("{\"message\": \"Invoice updated successfully!\"}");
                return;
            } 
        
            // sale
            message = createNewSale(selectedClientId, saleDate, cartArray, isCredit);
            String finalMessage = message.toString().trim().replace("\n", "\\n").replace("\"", "\\\"");
            
            if (finalMessage.isEmpty()) {
                String successMessage = isCredit ? "Credit sale submitted successfully!" : "Sale submitted successfully!";
                response.getWriter().write("{\"message\": \"" + successMessage + "\"}");
            } else {
                response.getWriter().write("{\"message\": \"" + finalMessage + "\"}");
            }
        }
        
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"Sale/Update error\", \"message\": \"" + e.getMessage() + "\"}");
        }        
    }

    private StringBuilder createNewSale(String selectedClientId, Date saleDate, JsonArray cartArray, boolean isCredit) 
        throws Exception 
    {
        Order order = new Order(1, selectedClientId, saleDate);
        order.insert(null);

        Order insertedOrder = new Order().getById(Order.getMaxId(), Order.class, null, "Orders");

        StringBuilder message = new StringBuilder();

        for (JsonElement cartItem : cartArray) {
            JsonObject item = cartItem.getAsJsonObject();
            int productId = item.get("productId").getAsInt();
            int quantity = item.get("quantity").getAsInt();

            Product product = new Product().getById(productId, Product.class, null);
            Stock.QuantityResult result = Stock.checkAndAdjustQuantity(product, quantity, saleDate);



            OrderItem orderItem = new OrderItem(1, insertedOrder, product, result.adjustedQuantity);
            orderItem.insert(null);

            Stock stock = new Stock(1, product, saleDate, 0, result.adjustedQuantity);
            stock.insert(null);

            if (result.excessQuantity > 0) {
                message.append("Stock shortage for product: " + product.getName() + 
                    ". Requested: " + quantity + ", Available: " + result.adjustedQuantity + "\n");
            }
        }

        SaleInvoice invoice = new SaleInvoice(1, insertedOrder, insertedOrder.getSumAmount(null));
        invoice.insert(null, isCredit);

        return message;
    }

    private void updateInvoice(int invoiceId, String updatedClientId, Date updatedSaleDate, JsonArray updatedCartArray, boolean isCredit) throws Exception {

        SaleInvoice existingInvoice = new SaleInvoice().getById(invoiceId, SaleInvoice.class, null, "SaleInvoices");

        Order existingOrder = existingInvoice.getOrder();

        existingOrder.setIdClient(updatedClientId);
        existingOrder.setDateOrder(updatedSaleDate);
        existingOrder.update(null);

        List<OrderItem> existingItems = existingOrder.getOrderItems(null);
        for (OrderItem item : existingItems) {
            Stock.reverseStockAdjustment(item.getProduct(), item.getQuantity(), existingOrder.getDateOrder());
            item.delete(null);
        }

        for (JsonElement cartItem : updatedCartArray) {
            JsonObject item = cartItem.getAsJsonObject();
            int productId = item.get("productId").getAsInt();
            int quantity = item.get("quantity").getAsInt();

            Product product = new Product().getById(productId, Product.class, null);
            Stock.QuantityResult result = Stock.checkAndAdjustQuantity(product, quantity, updatedSaleDate);

            OrderItem orderItem = new OrderItem(1, existingOrder, product, result.adjustedQuantity);
            orderItem.insert(null);

            Stock stock = new Stock(1, product, updatedSaleDate, 0, result.adjustedQuantity);
            stock.insert(null);
        }

        existingInvoice.setTotalAmount(existingOrder.getSumAmount(null));
        existingInvoice.update(null, isCredit);
    }


    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException 
    {
        // Handle CORS preflight request
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }       
}