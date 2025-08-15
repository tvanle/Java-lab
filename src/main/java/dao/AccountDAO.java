package dao;

import model.Account;
import util.PasswordUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO extends BaseDAO<Account> {
    
    public AccountDAO() {
        super();
    }
    
    public AccountDAO(Connection connection) {
        super(connection);
    }
    
    @Override
    public Account findById(int id) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractAccountFromResultSet(rs);
            }
        } finally {
            closeResources(rs, ps);
        }
        return null;
    }
    
    public Account findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE username = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractAccountFromResultSet(rs);
            }
        } finally {
            closeResources(rs, ps);
        }
        return null;
    }
    
    public Account findByCustomerId(int customerId) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE customer_id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, customerId);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractAccountFromResultSet(rs);
            }
        } finally {
            closeResources(rs, ps);
        }
        return null;
    }
    
    public Account authenticate(String username, String password) throws SQLException {
        Account account = findByUsername(username);
        if (account != null && account.isActive()) {
            if (account.getPassword().equals(password) || 
                PasswordUtil.verifyPassword(password, account.getPassword())) {
                updateLastLogin(account.getId());
                return account;
            }
        }
        return null;
    }
    
    @Override
    public List<Account> findAll() throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts ORDER BY id DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                accounts.add(extractAccountFromResultSet(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return accounts;
    }
    
    @Override
    public boolean insert(Account account) throws SQLException {
        String sql = "INSERT INTO accounts (customer_id, username, password, role, is_active) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        
        try {
            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, account.getCustomerId());
            ps.setString(2, account.getUsername());
            ps.setString(3, PasswordUtil.hashPassword(account.getPassword()));
            ps.setString(4, account.getRole());
            ps.setBoolean(5, account.isActive());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    account.setId(generatedKeys.getInt(1));
                }
                return true;
            }
        } finally {
            closeResources(ps);
        }
        return false;
    }
    
    @Override
    public boolean update(Account account) throws SQLException {
        String sql = "UPDATE accounts SET username = ?, role = ?, is_active = ? WHERE id = ?";
        PreparedStatement ps = null;
        
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getRole());
            ps.setBoolean(3, account.isActive());
            ps.setInt(4, account.getId());
            
            return ps.executeUpdate() > 0;
        } finally {
            closeResources(ps);
        }
    }
    
    public boolean updatePassword(int accountId, String newPassword) throws SQLException {
        String sql = "UPDATE accounts SET password = ? WHERE id = ?";
        PreparedStatement ps = null;
        
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, PasswordUtil.hashPassword(newPassword));
            ps.setInt(2, accountId);
            
            return ps.executeUpdate() > 0;
        } finally {
            closeResources(ps);
        }
    }
    
    public boolean updateLastLogin(int accountId) throws SQLException {
        String sql = "UPDATE accounts SET last_login = CURRENT_TIMESTAMP WHERE id = ?";
        PreparedStatement ps = null;
        
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, accountId);
            
            return ps.executeUpdate() > 0;
        } finally {
            closeResources(ps);
        }
    }
    
    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM accounts WHERE id = ?";
        PreparedStatement ps = null;
        
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } finally {
            closeResources(ps);
        }
    }
    
    public boolean isUsernameExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM accounts WHERE username = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } finally {
            closeResources(rs, ps);
        }
        return false;
    }
    
    private Account extractAccountFromResultSet(ResultSet rs) throws SQLException {
        return new Account(
            rs.getInt("id"),
            rs.getInt("customer_id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("role"),
            rs.getBoolean("is_active"),
            rs.getTimestamp("created_date"),
            rs.getTimestamp("last_login")
        );
    }
}