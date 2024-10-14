package ejb;

import java.util.List;
import client.Client;
import javax.ejb.*;

@Stateless
public class ClientServiceBean implements ClientServiceRemote {

    @Override
    public List<Client> getAllClients() 
        throws Exception
    {
        return Client.getAll(null);
    }

    @Override
    public Client getClientById(String id) 
        throws Exception 
    {
       return Client.getById(id, null);
    }
}