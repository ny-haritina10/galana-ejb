package pompiste;

import java.sql.*;
import base.BaseModel;

public class Pompiste extends BaseModel<Pompiste> {

    private int id;
    private String name;

    // Constructor
    public Pompiste() 
    { }

    public Pompiste(int id, String name) {
        setId(id);
        setName(name);
    }
    
    public int getId() 
    { return id; }

    public void setId(int id) {
        if (id > 0) 
        { this.id = id; } 
        
        else 
        { throw new IllegalArgumentException("ID must be positive."); }
    }

    public String getName()
    { return name; }

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty() && name.length() <= 255) 
        { this.name = name; } 
        
        else 
        { throw new IllegalArgumentException("Name cannot be null, empty, or longer than 255 characters."); }
    }

    @Override
    public String toString() 
    { return "Pompiste{id=" + id + ", name='" + name + "'}"; }

    @Override
    protected Pompiste mapRow(ResultSet result) throws Exception {
        int id = result.getInt("id");
        String name = result.getString("name");

        return new Pompiste(id, name);
    }
}