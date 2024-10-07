package ejb;

import java.util.List;

import javax.ejb.Remote;

import mg.cnaps.compta.ComptaSousEcriture;

@Remote
public interface EcritureServiceRemote {
    public void writeEntries(ComptaSousEcriture[] entries);
    public List<ComptaSousEcriture> getAllComptaSousEcriture() throws Exception; 
}