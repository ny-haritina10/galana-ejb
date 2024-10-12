package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ejb.EJB;

import ejb.EcritureServiceRemote;
import ejb.ClientServiceRemote;

import encaissement.Encaissement;
import mg.cnaps.compta.ComptaSousEcriture;

import prelevement.Prelevement;


public class EncaissementController extends HttpServlet {

    @EJB
    private EcritureServiceRemote remote;

    @EJB
    ClientServiceRemote remote;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException 
    {   
        try {
            Prelevement[] prelevements = new Prelevement().getAll(Prelevement.class, null);

            req.setAttribute("prelevements", prelevements);
            req.getRequestDispatcher("encaissement-form.jsp").forward(req, res);
        } 
        
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
        throws ServletException, IOException 
    {
        try {
            String idPrelevement = req.getParameter("id_prelevement");
            String montantEncaisseStr = req.getParameter("montant_encaisse");
            String dateEncaissementStr = req.getParameter("date_encaissement");

            double montantEncaisse = Double.parseDouble(montantEncaisseStr);

            java.sql.Date dateEncaissement = java.sql.Date.valueOf(dateEncaissementStr);

            Prelevement prelevement = new Prelevement().getById(Integer.valueOf(idPrelevement), Prelevement.class,null);

            Encaissement encaissement = new Encaissement();
            encaissement.setDateEncaissement(dateEncaissement);
            encaissement.setMontantEncaisse(montantEncaisse);
            encaissement.setPrelevement(prelevement);

            encaissement.insert(null); 

            List<Encaissement> listEncaisse = Encaissement.getAllEncaissementByIdPrelevement(prelevement.getId());
            List<ComptaSousEcriture> toEntries = Encaissement.encaissementToEntries(listEncaisse);
            
            ComptaSousEcriture[] entriesArray = toEntries.toArray(new ComptaSousEcriture[0]);
            remote.writeEntries(entriesArray);



            resp.sendRedirect("index.jsp"); 
        } 

        catch (Exception e) {
            e.printStackTrace();
        }
    }
}