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
import pompe.Pompe;
import pompiste.Pompiste;
import prelevement.Prelevement;
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
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

@WebServlet("/api/prelevement_lubrifiants")
public class PrelevementLubrifiantController extends HttpServlet {
    
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException 
    {
        try {
            Pompe[] pompeList = new Pompe().getAll(Pompe.class, null);
            Pompiste[] pompisteList = new Pompiste().getAll(Pompiste.class, null);
            Product[] products = new Product().getAll(Product.class, null);

            List<Product> filteredProduct = new ArrayList<Product>();

            for (Product product : products) {
                if (!product.getSousType().equals("UNDEFINED") || product.getTypeProduct().equals("UNDEFINED") || product.getTypeProduct().equals("MIXTE"))
                { filteredProduct.add(product); }
            }

            JsonObject jsonObject = new JsonObject();

            jsonObject.add("products", gson.toJsonTree(filteredProduct));
            jsonObject.add("pompes", gson.toJsonTree(pompeList));
            jsonObject.add("pompistes", gson.toJsonTree(pompisteList));

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");

            response.getWriter().write(gson.toJson(jsonObject));
        }
         
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException 
    {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            // Read the request body
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            String jsonString = jsonBuilder.toString();
            System.out.println("Received JSON Data: " + jsonString);

            JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
            
            int productId = jsonObject.get("productId").getAsInt();
            int pompeId = jsonObject.get("pompeId").getAsInt();
            int pompisteId = jsonObject.get("pompisteId").getAsInt();
            String quantity = jsonObject.get("quantity").getAsString();
            String datePrelevement = jsonObject.get("datePrelevement").getAsString();

            Pompiste pompiste = new Pompiste().getById(pompisteId, Pompiste.class, null);
            Pompe pompe = new Pompe().getById(pompeId, Pompe.class, null);
            Product product = new Product().getById(productId, Product.class, null);

            double doubleQte = Double.valueOf(quantity).doubleValue();

            Stock.QuantityResult result = Stock.checkAndAdjustQuantity(product, (int) doubleQte, java.sql.Date.valueOf(datePrelevement)); 

            JsonObject responseJson = new JsonObject();            
            
            if (result.excessQuantity > 0 && !product.getTypeProduct().equals("UNDEFINED")) {

                System.out.println(product.getName() + " " + doubleQte + " " + result.adjustedQuantity);
                String message = ("Stock shortage for Boutique Product: " + product.getName() +". Preleved:"+ doubleQte + ", Available: " + result.adjustedQuantity).trim();

                responseJson.addProperty("status", "error");
                responseJson.addProperty("message", message);
            } 
            
            
            else { 
                Prelevement prelevement = new Prelevement(0, pompiste, pompe, product, doubleQte, java.sql.Date.valueOf(datePrelevement));
                prelevement.insert(null);

                Integer idMaxPrelevement = Prelevement.getMaxId();
                Prelevement lastPrelev = new Prelevement().getById(idMaxPrelevement, Prelevement.class, null);

                if (idMaxPrelevement % 2 != 0) {     

                    
                    System.out.println("#====================================#");
                    System.out.println(product.getName() + " PU.V: " + product.getPuVente() + " DIFF QTE: " + lastPrelev.getPrelevementDifferenceQte() + " DIFF AMOUNT" + lastPrelev.getPrelevementDifference());
                    System.out.println("#====================================#");


                    Stock stock = new Stock(1, product, lastPrelev.getDatePrelevement(), 0, (int) lastPrelev.getPrelevementDifferenceQte());
                    stock.insert(null);
                }
                responseJson.addProperty("status", "success");
                responseJson.addProperty("message", "Prelevement success");
            }
            
            // Use Gson to properly format the JSON response
            response.getWriter().write(gson.toJson(responseJson));
        } 


        catch (Exception e) {
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("status", "error");

            // Clean up error message to remove any control characters
            String cleanErrorMessage = e.getMessage() != null ? 
                e.getMessage().replaceAll("[\\p{Cntrl}\\p{Space}]+", " ").trim() : 
                "An unknown error occurred";
            errorJson.addProperty("message", cleanErrorMessage);
            response.getWriter().write(gson.toJson(errorJson));
            e.printStackTrace();
        }        
    }
}