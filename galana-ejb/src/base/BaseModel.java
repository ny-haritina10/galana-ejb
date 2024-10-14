package base;

import java.sql.*;
import java.util.*;
import java.lang.reflect.Array;

import database.Database;

public abstract class BaseModel<T> {

    @SuppressWarnings("unchecked")
    public T[] getAll(Class<T> clazz, Connection con, String tableName)
        throws Exception
    {
        Vector<T> list= new Vector<T>();

        boolean valid = true;
        Statement state = null;
        ResultSet result = null;

        try {
            if(con == null) {
                con = Database.getConnection();
                valid = false;
            }

            String sql = "SELECT * FROM " + (tableName != null ? tableName : this.getClass().getSimpleName() + " ORDER BY id");
            state = con.createStatement();
            result = state.executeQuery(sql);

            while(result.next()) {
                T item = mapRow(result);
                list.add(item);
            }
        } 
        catch (Exception e) 
        { e.printStackTrace(); }
        finally {
            try {
                if (state != null) state.close(); 
                if (result != null ) result.close(); 
                if (valid == false || con !=null) con.close(); 
            } 
            catch (Exception e) 
            { e.printStackTrace(); }
        }

        T[] items = (T[]) Array.newInstance(clazz, list.size());
        list.toArray(items);

        return items;
    }

    public T[] getAll(Class<T> clazz, Connection con) 
        throws Exception 
    {
        return getAll(clazz, con, null);
    }

    public T getById(int id, Class<T> clazz, Connection con, String tableName) 
        throws Exception 
    {
        T item = null;
        boolean valid = true;
        PreparedStatement state = null;
        ResultSet result = null;

        try {
            if(con == null) {
                con = Database.getConnection();
                valid = false;
            }

            String actualTableName = tableName != null ? tableName : this.getClass().getSimpleName();
            String sql = "SELECT * FROM " + actualTableName + " WHERE id = ?";

            state = con.prepareStatement(sql);
            state.setInt(1, id);

            result = state.executeQuery();

            if(result.next()) 
            { item = mapRow(result); }
        } 
        catch (Exception e) 
        { e.printStackTrace(); } 
        finally {
            try {
                if (state != null) state.close(); 
                if (result != null) result.close(); 
                if (!valid || con != null) con.close(); 
            } 
            catch (Exception e) 
            { e.printStackTrace(); }
        }

        return item;
    }

    public T getById(int id, Class<T> clazz, Connection con) 
        throws Exception 
    {
        return getById(id, clazz, con, null);
    }

    protected abstract T mapRow(ResultSet result) throws Exception;
}