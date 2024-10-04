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
import mesurement.Mesure;
import mesurement.Mesurement;
import pompe.Pompe;

public class MesureController extends HttpServlet {

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
                req.setAttribute("succes", "Insertion Mesure Success");
                req.getRequestDispatcher("ecriture.jsp").forward(req, resp);
            } 
            
            else {
                req.setAttribute("error", "Error for the Insertion mesure");
                req.getRequestDispatcher("index.jsp").forward(req, resp);
            }
        } 
        
        catch (Exception e) 
        { e.printStackTrace(); } 
        
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