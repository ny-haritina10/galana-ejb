package controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ejb.EcritureServiceRemote;
import mg.cnaps.compta.ComptaSousEcriture;
import creance.Creance;

import javax.ejb.EJB;

public class CreanceController extends HttpServlet {

    @EJB
    private EcritureServiceRemote remoteBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException 
    {   
       res.sendRedirect("creance-form.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException 
    {
        try {
            List<ComptaSousEcriture> list = remoteBean.getAllComptaSousEcriture();
            
            String exerciceStr = req.getParameter("exercice");
            Integer exercice = null;
            
            if (exerciceStr != null && !exerciceStr.isEmpty()) {
                exercice = Integer.valueOf(exerciceStr);
            }

            Map<String, Double> creance = Creance.getCreanceSituation(list, exercice);
            double totalCreance = Creance.getTotalCreance(list, exercice);

            req.setAttribute("creance", creance);
            req.setAttribute("total-creance", totalCreance);
            req.setAttribute("exercice", exercice);
            
            req.getRequestDispatcher("creance.jsp").forward(req, res);
        } 
        catch (NumberFormatException e) {
            req.setAttribute("error", "Invalid exercise year. Please enter a valid number.");
            req.getRequestDispatcher("creance-form.jsp").forward(req, res);
        }
        catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "An error occurred while calculating accounts receivable.");
            req.getRequestDispatcher("creance-form.jsp").forward(req, res);
        }
    }
}