package api;

import com.google.gson.Gson;

import client.Client;
import ejb.ClientServiceBean;
import ejb.ClientServiceRemote;
import pompe.Pompe;
import pompiste.Pompiste;
import product.Product;

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

            request.setAttribute("pompes", pompeList);
            request.setAttribute("pompistes", pompisteList);
            request.setAttribute("products", products);

            request.getRequestDispatcher("prelevement.jsp").forward(req, res);
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

        }   

        catch (Exception e) {
            e.printStackTrace
        }        
    }       
}