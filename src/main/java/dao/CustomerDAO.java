package dao;

import model.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO extends BaseDAO<Customer> {
    
    public CustomerDAO() {
        super();
    }
    
    public CustomerDAO(Connection connection) {
        super(connection);
    }
    
    @Override
    public Customer findById(int id) throws SQLException {
        String sql = "SELECT * FROM customers WHERE id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractCustomerFromResultSet(rs);
            }
        } finally {
            closeResources(rs, ps);
        }
        return null;
    }
    
    public Customer findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM customers WHERE email = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractCustomerFromResultSet(rs);
            }
        } finally {
            closeResources(rs, ps);
        }
        return null;
    }
    
    @Override
    public List<Customer> findAll() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers ORDER BY id DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                customers.add(extractCustomerFromResultSet(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return customers;
    }
    
    @Override
    public boolean insert(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers (name, email, phone) VALUES (?, ?, ?)";
        PreparedStatement ps = null;
        
        try {
            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getPhone());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    customer.setId(generatedKeys.getInt(1));
                }
                return true;
            }
        } finally {
            closeResources(ps);
        }
        return false;
    }
    
    @Override
    public boolean update(Customer customer) throws SQLException {
        String sql = "UPDATE customers SET name = ?, email = ?, phone = ? WHERE id = ?";
        PreparedStatement ps = null;
        
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getPhone());
            ps.setInt(4, customer.getId());
            
            return ps.executeUpdate() > 0;
        } finally {
            closeResources(ps);
        }
    }
    
    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM customers WHERE id = ?";
        PreparedStatement ps = null;
        
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } finally {
            closeResources(ps);
        }
    }
    
    public List<Customer> search(String keyword) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE name LIKE ? OR email LIKE ? OR phone LIKE ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = connection.prepareStatement(sql);
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                customers.add(extractCustomerFromResultSet(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return customers;
    }
    
    private Customer extractCustomerFromResultSet(ResultSet rs) throws SQLException {
        return new Customer(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("phone"),
            rs.getTimestamp("created_date")
        );
    }
}