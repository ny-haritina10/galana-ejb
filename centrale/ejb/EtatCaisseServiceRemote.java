package ejb;

import java.sql.Date;
import javax.ejb.Remote;

@Remote
public interface EtatCaisseServiceRemote {
    public double getEtatDeCaisse(Date dateMin, Date dateMax) throws Exception;
}