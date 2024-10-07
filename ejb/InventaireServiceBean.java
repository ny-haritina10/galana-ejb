package ejb;

import java.sql.Connection;
import java.sql.Date;

import inventaire.Inventaire;
import inventaire.InventaireFille;
import utilitaire.UtilDB;

import javax.ejb.Stateless;


@Stateless
public class InventaireServiceBean implements InventaireServiceRemote {

    @Override
    public void saveInventory(String designation, String remarque, Date dateInventory, double quantity) {
        Connection connection = new UtilDB().GetConn();
        String user = "880679";

        try {
            Inventaire inventaire = new Inventaire();
            inventaire.setDesignation(designation);
            inventaire.setIdMagasin("PHARM001");        // default id magasin
            inventaire.setRemarque(remarque);
            inventaire.setDaty(dateInventory);

            inventaire.construirePK(connection);
            inventaire.createObject(user, connection);

            InventaireFille inventaireFille = new InventaireFille();
            inventaireFille.setIdInventaire(inventaire.getId());
            inventaireFille.setIdProduit("PRD004423");
            inventaireFille.setExplication(designation + " " + remarque + " : " + dateInventory);
            inventaireFille.setQuantite(quantity);
            inventaireFille.setIdJauge(null);
        } 
        
        catch (Exception e) 
        { e.printStackTrace(); }

        finally {
            try 
            { connection.close(); } 
            
            catch (Exception e) 
            { e.printStackTrace(); } 
        }
    }   
}