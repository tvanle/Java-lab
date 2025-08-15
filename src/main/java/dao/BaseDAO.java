package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import util.DBConnection;

public abstract class BaseDAO<T> {
    protected Connection connection;
    
    public BaseDAO() {
        try {
            this.connection = DBConnection.createConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public BaseDAO(Connection connection) {
        this.connection = connection;
    }
    
    public abstract T findById(int id) throws SQLException;
    public abstract List<T> findAll() throws SQLException;
    public abstract boolean insert(T entity) throws SQLException;
    public abstract boolean update(T entity) throws SQLException;
    public abstract boolean delete(int id) throws SQLException;
    
    protected void closeResources(ResultSet rs, PreparedStatement ps) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    protected void closeResources(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}