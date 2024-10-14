/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import pertegain.Tiers;
import utilitaire.UtilDB;

public class Client extends Tiers{

    private String telephone;

    private String remarque;

    public Client(){
        this.setNomTable("CLIENT");
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }   

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }
    
    @Override
    public void construirePK(Connection c) throws Exception {
        this.preparePk("CLI", "getSeqClient");
        this.setId(makePK(c));
    }
    
    
    @Override
    public String getValColLibelle() {
        return this.getNom();
    }

    public static List<Client> getAll(Connection c) 
        throws Exception 
    {
        if (c == null)
        { c = new UtilDB().GetConn(); }

        List<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM CLIENT";
        try (PreparedStatement stmt = c.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getString("id")); 
                client.setNom(rs.getString("nom"));
                client.setCompte(rs.getString("compte"));
                client.setAdresse(rs.getString("adresse"));
                client.setMail(rs.getString("mail"));
                client.setTelephone(rs.getString("telephone")); 
                client.setRemarque(rs.getString("remarque")); 

                clients.add(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Erreur lors de la récupération des clients: " + e.getMessage());
        }
        return clients;
    }

    public static Client getById(String id, Connection connection) 
        throws Exception
    {
        if (connection == null)
        { connection = new UtilDB().GetConn(); }

        for (Client client : Client.getAll(connection)) {
            if (client.getId().equals(id))
            { return client; }
        }

        return null;
    } 
}