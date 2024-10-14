package ejb;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import mg.cnaps.compta.ComptaSousEcriture;
import utilitaire.UtilDB;

import javax.ejb.Stateless;

@Stateless
public class EtatCaisseServiceBean implements EtatCaisseServiceRemote {

    @Override
    public double getEtatDeCaisse(Date dateMin, Date dateMax) throws Exception {
        double amount = 0;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String query = "SELECT ABS(SUM(debit) - SUM(credit)) AS etat_caisse " +
                    "FROM COMPTA_SOUS_ECRITURE cse " +
                    "WHERE COMPTE = '5300000000000' AND " +
                    "DATY BETWEEN ? AND ?";

        try {
            con = new UtilDB().GetConn();
            pstmt = con.prepareStatement(query);

            // Set the date parameters using java.sql.Date
            pstmt.setDate(1, new java.sql.Date(dateMin.getTime()));
            pstmt.setDate(2, new java.sql.Date(dateMax.getTime()));

            rs = pstmt.executeQuery();

            if (rs.next()) {
                amount = rs.getDouble("etat_caisse");
            }
        } 
        
        catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error while fetching state of caisse.", e);
        } 
        
        finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return amount;
    }   
}