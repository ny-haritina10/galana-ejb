package anomalie;

import java.sql.Date;
import java.sql.ResultSet;
import base.BaseModel;
import pompe.Pompe;

public class Anomalie extends BaseModel<Anomalie> {

    private Pompe pompe;
    private Date dateVente;
    private double prelevInL;      
    private double prelevementInCm; 
    private double mesureInL;     
    private double anomalieInL;    

    // Constructor
    public Anomalie() {}

    public Anomalie(Pompe pompe, Date dateVente, double prelevInL, double prelevementInCm, double mesureInCm, double anomalieInL) {
        this.pompe = pompe;
        this.dateVente = dateVente;
        this.prelevInL = prelevInL;
        this.prelevementInCm = prelevementInCm;
        this.mesureInL = mesureInCm;
        this.anomalieInL = anomalieInL;
    }

    // Getters and Setters
    public Pompe getPompe() { 
        return pompe; 
    }

    public void setPompe(Pompe pompe) { 
        this.pompe = pompe; 
    }

    public Date getDateVente() { 
        return dateVente; 
    }

    public void setDateVente(Date dateVente) { 
        this.dateVente = dateVente; 
    }

    public double getPrelevInL() { 
        return prelevInL; 
    }

    public void setPrelevInL(double prelevInL) { 
        this.prelevInL = prelevInL; 
    }

    public double getPrelevementInCm() { 
        return prelevementInCm; 
    }

    public void setPrelevementInCm(double prelevementInCm) { 
        this.prelevementInCm = prelevementInCm; 
    }

    public double getMesureInL() { 
        return mesureInL; 
    }

    public void setMesureInL(double mesureInL) { 
        this.mesureInL = mesureInL; 
    }

    public double getAnomalieInL() { 
        return anomalieInL; 
    }

    public void setAnomalieInL(double anomalieInL) { 
        this.anomalieInL = anomalieInL; 
    }

    // Override mapRow to map ResultSet rows to the view object
    @Override
    protected Anomalie mapRow(ResultSet resultSet) 
        throws Exception 
    {
        Pompe pompe = new Pompe().getById(resultSet.getInt("id_pompe"), Pompe.class, null);
        Date dateVente = resultSet.getDate("date_vente");
        double prelevInL = resultSet.getDouble("prelev_in_L");
        double prelevementInCm = resultSet.getDouble("prelevement_in_cm");
        double mesureInL = resultSet.getDouble("mesure_in_l");
        double anomalieInL = resultSet.getDouble("anomalie_in_L");

        return new Anomalie(pompe, dateVente, prelevInL, prelevementInCm, mesureInL, anomalieInL);
    }
}