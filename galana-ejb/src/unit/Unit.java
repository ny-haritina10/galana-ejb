package unit;

import java.sql.*;
import base.BaseModel;

public class Unit extends BaseModel<Unit> {

    private int id;
    private String name;

    // Constructor
    public Unit() 
    { }

    public Unit(int id, String name) {
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
    { return "Unit{id=" + id + ", name='" + name + "'}"; }

    @Override
    protected Unit mapRow(ResultSet resultSet) throws Exception {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");

        return new Unit(id, name);
    }
}