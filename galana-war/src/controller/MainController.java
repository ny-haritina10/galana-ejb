package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ejb.EcritureServiceRemote;
import prevision.Prevision;
import product.Product;
import stock.Stock;
import inventaire.Inventaire;
import inventaire.InventaireFille;
import invoice.SaleInvoice;
import magasin.Magasin;
import mg.cnaps.compta.ComptaSousEcriture;
import order.Order;
import order.OrderItem;
import utilitaire.UtilDB;

import javax.ejb.EJB;

public class MainController extends HttpServlet {

    @EJB
    private EcritureServiceRemote remoteBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException 
    {
        try {
            res.getWriter().println("Main Controller doGet");
        } 

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
        throws ServletException, IOException 
    {
        // Set response content type
        resp.setContentType("application/json");
        
        // read data sent from the React Native app
        String name = req.getParameter("name");
        String email = req.getParameter("email");

        System.out.println("Received Name: " + name);
        System.out.println("Received Email: " + email);

        // send a response back to the client
        PrintWriter out = resp.getWriter();
        out.print("{\"status\":\"success\"}");
        out.flush();
    }
}