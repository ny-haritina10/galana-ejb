package ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import mg.cnaps.compta.ComptaSousEcriture;
import utilitaire.UtilDB;
import javax.ejb.Stateless;

@Stateless
public class EcritureServiceBean implements EcritureServiceRemote {

    @Override
    public void writeEntries(ComptaSousEcriture[] entries) {

        Connection connection = new UtilDB().GetConn();

        try {
            String user = "880679";

            for (ComptaSousEcriture comptaSousEcriture : entries) {
                comptaSousEcriture.createObject(user, connection);
            } 
            System.out.println("Print from Ecriture Bean Centrale");   
        } 
        
        catch (Exception e) 
        { e.printStackTrace(); }

        finally {
            if (connection != null) {
                try 
                { connection.close(); } 
                
                catch (Exception ex) 
                { ex.printStackTrace(); }
            }
        }
    }

    @Override
    public List<ComptaSousEcriture> getAllComptaSousEcriture() 
        throws Exception 
    {
        Connection conn = new UtilDB().GetConn();

        List<ComptaSousEcriture> comptaSousEcritures = new ArrayList<>();
        
        String query = "SELECT * FROM compta_sous_ecriture";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ComptaSousEcriture comptaSousEcriture = new ComptaSousEcriture();
                comptaSousEcriture.setId(rs.getString("id"));
                comptaSousEcriture.setCompte(rs.getString("compte"));
                comptaSousEcriture.setDebit(rs.getDouble("debit"));
                comptaSousEcriture.setCredit(rs.getDouble("credit"));
                comptaSousEcriture.setRemarque(rs.getString("remarque"));
                comptaSousEcriture.setLibellePiece(rs.getString("libellePiece"));
                comptaSousEcriture.setIdMere(rs.getString("idMere"));
                comptaSousEcriture.setReference_engagement(rs.getString("reference_engagement"));
                comptaSousEcriture.setCompte_aux(rs.getString("compte_aux"));
                comptaSousEcriture.setLettrage(rs.getString("lettrage"));
                comptaSousEcriture.setJournal(rs.getString("journal"));
                comptaSousEcriture.setExercice(rs.getInt("exercice"));
                comptaSousEcriture.setFolio(rs.getString("folio"));
                comptaSousEcriture.setDaty(rs.getDate("daty"));
                comptaSousEcriture.setAnalytique(rs.getString("analytique"));
                comptaSousEcriture.setSource(rs.getString("source"));

                comptaSousEcritures.add(comptaSousEcriture);
            }
        } 
        
        catch (Exception e) {
            throw new Exception("Erreur lors de la récupération des compta sous écritures: " + e.getMessage());
        }

        System.out.println("Print from Ecriture Bean Centrale");   
        
        return comptaSousEcritures;
    }
}