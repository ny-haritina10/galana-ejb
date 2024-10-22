package api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

@WebServlet("/api/cash")
public class CashStatusController extends HttpServlet {

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

            SaleInvoice[] invoicesDetail = new SaleInvoice().getAll(SaleInvoice.class, null, "SALEINVOICES");

            String jsonResponse = gson.toJson(invoicesDetail);
            response.getWriter().write(jsonResponse);
        }    
        
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}