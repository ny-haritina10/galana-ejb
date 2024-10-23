package api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import client.Client;
import ejb.ClientServiceBean;
import ejb.ClientServiceRemote;
import ejb.EcritureServiceBean;
import ejb.EcritureServiceRemote;
import encaissement.Encaissement;

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

import mg.cnaps.compta.ComptaSousEcriture;

@WebServlet("/api/encaissement_lubrifiants")
public class EncaissementLubrifiantController extends HttpServlet {

    @EJB
    private EcritureServiceRemote remote = new EcritureServiceBean();

    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException 
    {   
        try {
            Prelevement[] prelevements = new Prelevement().getAll(Prelevement.class, null);

            JsonObject jsonObject = new JsonObject();
            jsonObject.add("prelevements", gson.toJsonTree(prelevements));

            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            res.setHeader("Access-Control-Allow-Origin", "*");
            res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");

            res.getWriter().write(gson.toJson(jsonObject));
        } 
        
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException 
    {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            String jsonString = jsonBuilder.toString();
            
            System.out.println("Received Encaissement Data: " + jsonString);

            JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
            
            int prelevementId = jsonObject.get("prelevementId").getAsInt();
            double montantEncaisser = jsonObject.get("montantEncaisser").getAsDouble();
            String dateEncaissementStr = jsonObject.get("dateEncaissement").getAsString();

            java.sql.Date dateEncaissement = java.sql.Date.valueOf(dateEncaissementStr);

            Prelevement prelevement = new Prelevement().getById(prelevementId, Prelevement.class,null);

            Encaissement encaissement = new Encaissement();
            encaissement.setDateEncaissement(dateEncaissement);
            encaissement.setMontantEncaisse(montantEncaisser);
            encaissement.setPrelevement(prelevement);

            encaissement.insert(null); 

            List<Encaissement> listEncaisse = Encaissement.getAllEncaissementByIdPrelevement(prelevement.getId());
            List<ComptaSousEcriture> toEntries = Encaissement.encaissementToEntries(listEncaisse);
            
            ComptaSousEcriture[] entriesArray = toEntries.toArray(new ComptaSousEcriture[0]);
            remote.writeEntries(entriesArray);
        } 
        
        catch (Exception e) {
            e.printStackTrace();
        }        
    }
}