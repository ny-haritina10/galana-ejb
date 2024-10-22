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

import ejb.EcritureServiceRemote;
import mg.cnaps.compta.ComptaSousEcriture;

import javax.ejb.EJB;

public class EcritureController extends HttpServlet {

    @EJB
    private EcritureServiceRemote remoteBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException 
    {
        try {
            List<ComptaSousEcriture> list = remoteBean.getAllComptaSousEcriture();

            req.setAttribute("ecriture", list);
            req.getRequestDispatcher("ecriture.jsp").forward(req, res);
        } 
        
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}