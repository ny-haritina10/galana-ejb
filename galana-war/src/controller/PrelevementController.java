package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.Database;
import pompe.Pompe;
import pompiste.Pompiste;
import prelevement.Prelevement;
import product.Product;

public class PrelevementController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException 
    {   
        try {
            Pompe[] pompeList = new Pompe().getAll(Pompe.class, null);
            Pompiste[] pompisteList = new Pompiste().getAll(Pompiste.class, null);
            Product[] products = new Product().getAll(Product.class, null);

            req.setAttribute("pompes", pompeList);
            req.setAttribute("pompistes", pompisteList);
            req.setAttribute("products", products);

            req.getRequestDispatcher("prelevement.jsp").forward(req, res);
        } 

        catch (Exception e) 
        { e.printStackTrace(); }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException 
    {
        Connection connection = null;

        try {
            int pompisteId = Integer.parseInt(req.getParameter("pompiste"));
            int pompeId = Integer.parseInt(req.getParameter("pompe"));
            int productId = Integer.parseInt(req.getParameter("product"));  
            double amount = Double.parseDouble(req.getParameter("amount"));
            Date datePrelevement = Date.valueOf(req.getParameter("date_prelevement"));

            Pompiste pompiste = new Pompiste().getById(pompisteId, Pompiste.class, connection);
            Pompe pompe = new Pompe().getById(pompeId, Pompe.class, connection);
            Product product = new Product().getById(productId, Product.class, connection);

            Prelevement prelevement = new Prelevement(0, pompiste, pompe, product, amount, datePrelevement);

            prelevement.insert(null);
            resp.sendRedirect("index.jsp");
        } 
        
        catch (Exception e) 
        {
            req.setAttribute("error", "An error occured : " + e.getMessage()); 
            req.getRequestDispatcher("prelevement.jsp").forward(req, resp);
            
            //throw e; 
        }
        
        finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}