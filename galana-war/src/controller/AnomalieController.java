package controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import anomalie.Anomalie;

import java.io.IOException;

public class AnomalieController extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException 
    {   
        try {
            Anomalie[] anomalieList = new Anomalie().getAll(Anomalie.class, null, "v_anomalie");

            req.setAttribute("anomalies", anomalieList);
            req.getRequestDispatcher("anomalie.jsp").forward(req, res);
        } 

        catch (Exception e) 
        { e.printStackTrace(); }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) 
    throws ServletException, IOException 
    {
        
    }    
}