package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ejb.EcritureServiceRemote;
import mg.cnaps.compta.ComptaSousEcriture;

import javax.ejb.EJB;

public class MainController extends HttpServlet {

    @EJB
    private EcritureServiceRemote remoteBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException 
    {
        try {
            ComptaSousEcriture sous = new ComptaSousEcriture("712000", 0.0, 1000.0, "//////////// Vente de Produit", 
                "///////////// Vente Carburant", null, null, null, "COMP000039", "2024", null, Date.valueOf("2024-01-01"), null, null);

            ComptaSousEcriture sous1 = new ComptaSousEcriture("712000", 1000.0, 0.0, "//////////// Client de Produit", 
                "///////////// Client Carburant", null, null, null, "COMP000039", "2024", null, Date.valueOf("2024-01-01"), null, null);

            remoteBean.writeEntries(new ComptaSousEcriture[] { sous, sous1 });

            PrintWriter out = res.getWriter();
            out.println("METY RANGAH IO");
        } 
        
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
        throws ServletException, IOException 
    {
     
    }
}