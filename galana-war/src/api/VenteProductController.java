package api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import product.Product;
import order.Order;
import order.OrderItem;
import stock.Stock;
import vente.Vente;
import invoice.SaleInvoice;
import vente.Vente;

import javax.servlet.ServletException;   
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;   
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;   
import java.io.InputStreamReader;
import java.sql.Date;  

@WebServlet("/api/vente_products")
public class VenteProductController extends HttpServlet {

    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException 
    {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");

            Vente[] ventes = new Vente().getAll(Vente.class, null, "V_VENTES_PER_POMPE_AND_DATE");

            String jsonResponse = gson.toJson(ventes);
            response.getWriter().write(jsonResponse);
        }    
        
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}