package ejb;

import java.util.List;
import javax.ejb.Remote;

import client.Client;

@Remote
public interface ClientServiceRemote {
    public List<Client> getAllClients() throws Exception;
    public Client getClientById(String id) throws Exception;
}