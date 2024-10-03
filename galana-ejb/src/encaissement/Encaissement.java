package encaissement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import base.BaseModel;
import database.Database;
import mg.cnaps.compta.ComptaSousEcriture;
import prelevement.Prelevement;

public class Encaissement extends BaseModel<Encaissement> {

    private int id;
    private Prelevement prelevement;
    private double montantEncaisse;
    private Date dateEncaissement;

    // Constructor
    public Encaissement() {}

    public Encaissement(int id, Prelevement prelevement, double montantEncaisse, Date dateEncaissement) {
        setId(id);
        setPrelevement(prelevement);
        setMontantEncaisse(montantEncaisse);
        setDateEncaissement(dateEncaissement);
    }

    public static List<Encaissement> getAllEncaissementByIdPrelevement(int idPrelevement) 
        throws Exception 
    {
        List<Encaissement> encaissements = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
    
        try {
            connection = Database.getConnection();
            String sql = "SELECT * FROM Encaissement WHERE id_prelevement = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, idPrelevement);
            resultSet = statement.executeQuery();
    
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Prelevement prelevement = new Prelevement().getById(idPrelevement, Prelevement.class, null);
                double montantEncaisse = resultSet.getDouble("montant_encaisse");
                Date dateEncaissement = resultSet.getDate("date_encaissement");
    
                Encaissement encaissement = new Encaissement(id, prelevement, montantEncaisse, dateEncaissement);
                encaissements.add(encaissement);
            }
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    
        return encaissements;
    }    

    public void insert(Connection connection) 
        throws Exception 
    {
        PreparedStatement statement = null;

        try {
            if (connection == null) 
            { connection = Database.getConnection(); }

            String sql = "INSERT INTO Encaissement (id, id_prelevement, montant_encaisse, date_encaissement) VALUES (seq_encaissement.NEXTVAL, ?, ?, ?)";

            statement = connection.prepareStatement(sql);

            statement.setInt(1, this.prelevement.getId()); 
            statement.setDouble(2, this.montantEncaisse);
            statement.setDate(3, new java.sql.Date(this.dateEncaissement.getTime()));

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Inserting Encaissement failed, no rows affected.");
            }
        } 
        
        finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<ComptaSousEcriture> encaissementToEntries(List<Encaissement> encaissements) 
        throws Exception 
    {
        Encaissement encaissement = encaissements.get(encaissements.size() - 1);

        double sumEncaisse = 0;
        for (Encaissement enc : encaissements) 
        { sumEncaisse += enc.getMontantEncaisse(); }

        if (encaissement != null) {
            List<ComptaSousEcriture> listSousEcriture = new ArrayList<ComptaSousEcriture>();

            double prelevement = encaissement.getPrelevement().getPrelevementDifference();
            double encaisse = encaissement.getMontantEncaisse();
            double impaye = Math.abs(prelevement - encaisse);

            Date date = encaissement.getDateEncaissement();

            if (encaissements.size() == 1) {
                System.out.println("prel" + prelevement + " encaisse: " + encaisse + " impaye: " + impaye);
            
    
                /* FACTURE PRELEVEMENT */
                ComptaSousEcriture first = new ComptaSousEcriture("4110000000000",prelevement,0, " %%% Vente Client du " + date," %%% Vente Client du " + date, null, null, null, "COMP000044", "2024", null, date, null, null);
                ComptaSousEcriture second = new ComptaSousEcriture("712000",  0,prelevement, " %%% Vente preleve du " + date, " %%% Vente preleve du " + date, null, null, null, "COMP000039", "2024", null, date, null, null);
    
                /* ENCAISSEMENT */
                ComptaSousEcriture fifth = new ComptaSousEcriture("4110000000000", 0,encaisse, " %%% Encaissement montant client du " + date," %%% Encaissement montant client du " + date, null, null, null, "COMP000044", "2024", null, date, null, null); 

                ComptaSousEcriture sixth = new ComptaSousEcriture("5300000000000",  encaisse,0, " %%% Debit Caisse du " + date, " %%% Debit Caisse du " + date,null, null, null, "COMP000036", "2024", null, date,null, null);
    
                listSousEcriture.add(first);
                listSousEcriture.add(second);
                listSousEcriture.add(fifth);
                listSousEcriture.add(sixth);
            }

            else {
                /* ENCAISSEMENT */
                ComptaSousEcriture fifth = new ComptaSousEcriture("4110000000000", 0,encaisse, " %%% Encaissement montant client du " + date," %%% Encaissement montant client du " + date, null, null, null, "COMP000044", "2024", null, date, null, null); 
                ComptaSousEcriture sixth = new ComptaSousEcriture("5300000000000",  0,encaisse, " %%% Debit Caisse du " + date, " %%% Debit Caisse du " + date,null, null, null, "COMP000036", "2024", null, date,null, null);

                listSousEcriture.add(fifth);
                listSousEcriture.add(sixth);
            }


           
            if (encaissement.getPrelevement().getAmount() > sumEncaisse) {
                /* FACTURE AVOIR */
                ComptaSousEcriture third = new ComptaSousEcriture("4110000000000", 0,impaye, " %%% Avoir client du " + date, " %%% Avoir client du " + date, null, null, null, "COMP000044", "2024", null, date,null,null);
                ComptaSousEcriture fourth = new ComptaSousEcriture("712000", impaye,0, " %%% Vente facture avoir du " + date," %%% Vente facture avoir du " + date, null, null, null,"COMP000039", "2024", null, date, null, null);
                ComptaSousEcriture fourthII = new ComptaSousEcriture("712000", 0,impaye, " %%% Vente facture avoir du " + date," %%% Vente facture avoir du " + date, null, null, null,"COMP000039", "2024", null, date, null, null);

                /* FACTURE CLIENT PARTICULIER */
                ComptaSousEcriture eight = new ComptaSousEcriture("4110000000000", impaye,0, " %%% Impaye Client du " + date, " %%% Impaye Client du " + date, null, null, null, "COMP000044", "2024", null, date, null, null);

                
                listSousEcriture.add(third);
                listSousEcriture.add(fourth);
                listSousEcriture.add(fourthII);
                listSousEcriture.add(eight);
            }

            return listSousEcriture;
        }
        
        return null;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id > 0) {
            this.id = id;
        } else {
            throw new IllegalArgumentException("ID must be positive.");
        }
    }

    public Prelevement getPrelevement() {
        return prelevement;
    }

    public void setPrelevement(Prelevement prelevement) {
        this.prelevement = prelevement;
    }

    public double getMontantEncaisse() {
        return montantEncaisse;
    }

    public void setMontantEncaisse(double montantEncaisse) {
        if (montantEncaisse >= 0) {
            this.montantEncaisse = montantEncaisse;
        } else {
            throw new IllegalArgumentException("Montant encaisse must be non-negative.");
        }
    }

    public Date getDateEncaissement() {
        return dateEncaissement;
    }

    public void setDateEncaissement(Date dateEncaissement) {
        if (dateEncaissement != null) {
            this.dateEncaissement = dateEncaissement;
        } else {
            throw new IllegalArgumentException("Encaissement date cannot be null.");
        }
    }

    @Override
    public String toString() {
        return "Encaissement{id=" + id + ", prelevement=" + prelevement + 
                ", montantEncaisse=" + montantEncaisse + 
                ", dateEncaissement=" + dateEncaissement + "}";
    }

    @Override
    protected Encaissement mapRow(ResultSet resultSet) 
        throws Exception 
    {
        int id = resultSet.getInt("id");
        Prelevement prelevement = new Prelevement().getById(resultSet.getInt("id_prelevement"), Prelevement.class, null);
        double montantEncaisse = resultSet.getDouble("montant_encaisse");
        Date dateEncaissement = resultSet.getDate("date_encaissement");

        return new Encaissement(id, prelevement, montantEncaisse, dateEncaissement);
    }
}