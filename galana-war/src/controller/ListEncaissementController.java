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

import encaissement.Encaissement;

public class ListEncaissementController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException 
    {   
        try {
            Encaissement[] encaissements = new Encaissement().getAll(Encaissement.class, null);

            req.setAttribute("encaissements", encaissements);
            req.getRequestDispatcher("list-encaissement.jsp").forward(req, res);
        } 
        
        catch (Exception e) 
        { e.printStackTrace(); }
    }
}