package controller;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ejb.EcritureServiceRemote;
import ejb.EtatCaisseServiceRemote;
import mg.cnaps.compta.ComptaSousEcriture;
import caisse.EtatDeCaisse;

public class EtatCaisseController extends HttpServlet {

    @EJB
    private EtatCaisseServiceRemote remote;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException 
    {
        try {
            String dateMinStr = req.getParameter("date-min");
            String dateMaxStr = req.getParameter("date-max");
    
            Date dateMin = Date.valueOf(dateMinStr);
            Date dateMax = Date.valueOf(dateMaxStr);
    
    
            double amount = 0;
            amount += remote.getEtatDeCaisse(dateMin, dateMax); 
            
            req.setAttribute("exercice", dateMax.getYear() + 1900); 
            req.setAttribute("dateMin", dateMin);
            req.setAttribute("dateMax", dateMax);
            req.setAttribute("amount", amount);
    
            req.getRequestDispatcher("etat-caisse.jsp").forward(req, res);
        } 
        
        catch (Exception e) 
        {
            req.setAttribute("error", "An error occured : " + e.getMessage()); 
            req.getRequestDispatcher("etat-caisse.jsp").forward(req, res);
            
            e.printStackTrace(); 
        }
    }
}