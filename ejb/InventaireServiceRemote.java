package ejb;

import java.sql.Date;

import javax.ejb.Remote;

@Remote
public interface InventaireServiceRemote {
    public void saveInventory(String designation, String remarque, Date dateInventory, double quantity);
}