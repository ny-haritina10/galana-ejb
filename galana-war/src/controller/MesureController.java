package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.Database;
import ejb.InventaireServiceRemote;
import mesurement.Mesure;
import mesurement.Mesurement;
import pompe.Pompe;

import javax.ejb.EJB;

public class MesureController extends HttpServlet {

    @EJB
    private InventaireServiceRemote beanRemote;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException 
    {   
        try {
            Pompe[] pompeList = new Pompe().getAll(Pompe.class, null);

            req.setAttribute("pompes", pompeList);
            req.getRequestDispatcher("mesure.jsp").forward(req, res);
        } 

        catch (Exception e) 
        { 
            e.printStackTrace(); 
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException 
    {
        Connection connection = null;

        try {
            int pompeId = Integer.parseInt(req.getParameter("pompe"));
            String dateMesureStr = req.getParameter("date_mesure");
            String mesureStr = req.getParameter("mesure");
            Date dateMesure = Date.valueOf(dateMesureStr);

            Pompe pompe = new Pompe().getById(pompeId, Pompe.class, null);
            Mesure mes = Mesure.parse(mesureStr);

            Mesurement mesurement = new Mesurement(0, pompe, dateMesure, mes);
            boolean success = mesurement.insert(null);

            if (success) {
                
                // save into inventory
                beanRemote.saveInventory(
                    "%% Inventaire du " + dateMesureStr + " pour la mesure de la pompe " + pompe.getName(),
                    "%% Mesure du " + dateMesureStr + " de " + mes.getValueInCM() + " cm",
                    dateMesure, 
                    mes.getValueInCM() 
                );
                
                req.setAttribute("succes", "Insertion Mesure Success");
                req.getRequestDispatcher("index.jsp").forward(req, resp);
            } 
            
            else {
                req.setAttribute("error", "Error for the Insertion mesure");
                req.getRequestDispatcher("index.jsp").forward(req, resp);
            }
        } 
        
        catch (Exception e) 
        {
            req.setAttribute("error", "An error occured : " + e.getMessage()); 
            req.getRequestDispatcher("mesure.jsp").forward(req, resp);
            
            e.printStackTrace(); 
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