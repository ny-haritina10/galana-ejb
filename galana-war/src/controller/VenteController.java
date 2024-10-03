package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vente.Vente;


public class VenteController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
         throws ServletException, IOException 
    {   
        try {
            Vente[] ventes = new Vente().getAll(Vente.class, null, "v_ventes_per_pompe_and_date");

            req.setAttribute("ventes", ventes);
            req.getRequestDispatcher("vente.jsp").forward(req, res);
        } 
        
        catch (Exception e) 
        { e.printStackTrace(); }
    }   
}
