package mesurement;

import java.sql.*;
import base.BaseModel;
import database.Database;
import pompe.Pompe;

public class Mesurement extends BaseModel<Mesurement> {

    private int id;
    private Pompe pompe;
    private Date dateMesurement;  
    private Mesure mesure;

    // Constructor
    public Mesurement() {}

    public Mesurement(int id, Pompe pompe, Date dateMesurement, Mesure mesure) 
        throws Exception
    {
        setId(id);
        setPompe(pompe);
        setDateMesurement(dateMesurement);
        setMesure(mesure);
    }

    public boolean insert(Connection connection) 
        throws Exception 
    {
        if (connection == null) 
        { connection = Database.getConnection(); };

        PreparedStatement statement = null;

        try {
            connection = Database.getConnection();

            String sql = "INSERT INTO Mesurement (id, id_pompe, date_mesurement, mesure) VALUES (seq_mesurement.NEXTVAL, ?, ?, ?)";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, this.pompe.getId());
            statement.setDate(2, this.dateMesurement);
            statement.setDouble(3, this.mesure.getValueInCM());

            System.out.println(this.pompe.getId() + " | " + this.getDateMesurement() + " | " + this.mesure.getValueInCM());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;

        } 
        
        finally {
            if (statement != null) 
            { statement.close(); }

            if (connection != null)
            { connection.close(); }
        }
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id >= 0) {
            this.id = id;
        } else {
            throw new IllegalArgumentException("ID must be positive.");
        }
    }

    public Pompe getPompe() {
        return pompe;
    }

    public void setPompe(Pompe pompe) {
        this.pompe = pompe;
    }

    public Date getDateMesurement() {
        return dateMesurement;
    }

    public void setDateMesurement(Date dateMesurement) {
        if (dateMesurement != null) {
            this.dateMesurement = dateMesurement;
        } else {
            throw new IllegalArgumentException("Date of measurement cannot be null.");
        }
    }

    public Mesure getMesure() 
    { return mesure; }

    public void setMesure(Mesure mesure) 
        throws Exception
    {
        if (mesure.getValueInCM() < 0)
        { throw new Exception("Invalid number: " + mesure); }

        this.mesure = mesure;
    }

    @Override
    public String toString() {
        return "Mesurement{id=" + id + ", pompe=" + pompe + ", dateMesurement=" + dateMesurement + ", mesure=" + mesure + "}";
    }

    @Override
    protected Mesurement mapRow(ResultSet resultSet) 
        throws Exception 
    {
        int id = resultSet.getInt("id");
        Pompe pompe = new Pompe().getById(resultSet.getInt("id_pompe"), Pompe.class, null);
        Date dateMesurement = resultSet.getDate("date_mesurement");  
        double mesureFromDB = resultSet.getDouble("mesure");
        Mesure mesure = new Mesure(mesureFromDB, "cm");     // value from DB is always CM

        return new Mesurement(id, pompe, dateMesurement, mesure);
    }
}