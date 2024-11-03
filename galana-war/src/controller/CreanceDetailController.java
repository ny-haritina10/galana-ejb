package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import client.Client;
import creance.DetailCreance;
import ejb.ClientServiceRemote;
import ejb.EcritureServiceRemote;
import encaissement.Encaissement;
import mg.cnaps.compta.ComptaSousEcriture;

import javax.ejb.EJB;

public class CreanceDetailController extends HttpServlet {

    @EJB
    ClientServiceRemote remote;

    @EJB
    EcritureServiceRemote ecritureRemote;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException 
    {   
        try {
            if (req.getParameter("mode") != null) {
                DetailCreance[] details = new DetailCreance().getAll(DetailCreance.class, null, "Creance");

                req.setAttribute("detail-creances", details);
                req.getRequestDispatcher("list-detail-creance.jsp").forward(req, res);
            }

            else {
                int id = Integer.valueOf((String) req.getParameter("id_encaissement"));
                Encaissement encaissement = new Encaissement().getById(id, Encaissement.class, null);
                List<Client> clients = remote.getAllClients();

                DetailCreance[] details = new DetailCreance().getAll(DetailCreance.class, null, "Creance");

                List<DetailCreance> concernedCreances = new ArrayList<>();
                for (DetailCreance detail : details) {
                    if (detail.getPrelevement().getId() == encaissement.getPrelevement().getId())
                    { concernedCreances.add(detail); }
                }

                HttpSession session = req.getSession();
                session.setAttribute("id_encaissement_session", encaissement.getId());

                req.setAttribute("concerned-creances", concernedCreances);
                req.setAttribute("encaissement", encaissement);
                req.setAttribute("clients", clients);
                
                req.getRequestDispatcher("detail-creance.jsp").forward(req, res);
            }
        } 
        
        catch (Exception e) 
        { e.printStackTrace(); }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException 
    {   
        try {
            HttpSession session = req.getSession();

            int id = Integer.valueOf((int) session.getAttribute("id_encaissement_session"));
            Encaissement encaissement = new Encaissement().getById(id, Encaissement.class, null);
            Date dateEcheance = Date.valueOf(req.getParameter("date-echeance"));
            String idClient = req.getParameter("client");
            Client client = remote.getClientById(idClient);

            double amount = Double.valueOf(req.getParameter("amount"));
            
            DetailCreance[] details = new DetailCreance().getAll(DetailCreance.class, null, "Creance");
            List<Client> clients = remote.getAllClients();
            List<DetailCreance> concernedCreances = new ArrayList<>();
            
            for (DetailCreance detail : details) {
                if (detail.getPrelevement().getId() == encaissement.getPrelevement().getId())
                { concernedCreances.add(detail); }
            }
          
            double payedAmount = encaissement.getMontantEncaisse();
            double amountToPay = encaissement.getPrelevement().getPrelevementDifference();
            double amountConcerned = DetailCreance.getSumAmountConcernedDetailCreances(concernedCreances);
            double unpayedAmount =  (amountToPay - payedAmount);
            double unpayedAmountDetailled = (amountToPay - payedAmount) - amountConcerned;

            if (amount > unpayedAmountDetailled) {
                req.setAttribute("error", "amount:  $" + amount + " is greater than the remaining amount to pay : $" + unpayedAmountDetailled);
                req.setAttribute("concerned-creances", concernedCreances);
                req.setAttribute("encaissement", encaissement);
                req.setAttribute("clients", clients);

                req.getRequestDispatcher("detail-creance.jsp").forward(req, res);
            }

            else {
                // creance inserted successfully
                DetailCreance detailCreance = new DetailCreance();
                            
                detailCreance.setAmount(amount);
                detailCreance.setDateEcheance(dateEcheance);
                detailCreance.setIdClient(idClient);
                detailCreance.setPrelevement(encaissement.getPrelevement());

                // insert detail creance
                detailCreance.insert(null);

                List<ComptaSousEcriture> entries = Encaissement.makePrevision(client, dateEcheance, amount);
                ComptaSousEcriture[] ecritures = entries.toArray(new ComptaSousEcriture[0]);

                ecritureRemote.writeEntries(ecritures);
                res.sendRedirect("index.jsp");
            }
        } 
        
        catch (Exception e) 
        {
            req.setAttribute("error", "An error occured : " + e.getMessage()); 
            req.getRequestDispatcher("detail-creance.jsp").forward(req, res);
            
            e.printStackTrace(); 
        }
    }
}