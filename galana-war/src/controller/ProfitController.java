package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ejb.EcritureServiceRemote;
import mg.cnaps.compta.ComptaSousEcriture;
import profit.Profit;

import javax.ejb.EJB;

public class ProfitController extends HttpServlet {

    @EJB
    private EcritureServiceRemote remoteBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException 
    {
        try {
            res.sendRedirect("profit-form.jsp");
        } 
        
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException 
    {
        try {
            List<ComptaSousEcriture> list = remoteBean.getAllComptaSousEcriture();
            String yearStr = req.getParameter("year");
            String monthStr = req.getParameter("month");
            
            double profit = 0;
            String periodDescription = "";
            Map<String, Double> revenueDetails = new HashMap<>();
            Map<String, Double> expenseDetails = new HashMap<>();
            
            if (yearStr != null && !yearStr.isEmpty()) {
                int year = Integer.parseInt(yearStr);
                
                if (monthStr != null && !monthStr.isEmpty()) {
                    int month = Integer.parseInt(monthStr);
                    profit = Profit.getProfitByMonth(list, year, month, revenueDetails, expenseDetails);
                    periodDescription = String.format("%d-%02d", year, month);
                } else {
                    profit = Profit.getProfitByExercice(list, year, revenueDetails, expenseDetails);
                    periodDescription = String.valueOf(year);
                }
            } else {
                profit = Profit.getProfit(list, revenueDetails, expenseDetails);
                periodDescription = "All time";
            }
            
            req.setAttribute("profit", profit);
            req.setAttribute("period", periodDescription);
            req.setAttribute("revenueDetails", revenueDetails);
            req.setAttribute("expenseDetails", expenseDetails);
            req.getRequestDispatcher("profit.jsp").forward(req, res);
        } 
        catch (NumberFormatException e) {
            req.setAttribute("error", "Invalid year or month. Please enter valid numbers.");
            req.getRequestDispatcher("profit-form.jsp").forward(req, res);
        }
        catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "An error occurred while calculating profit.");
            req.getRequestDispatcher("profit-form.jsp").forward(req, res);
        }
    }
}