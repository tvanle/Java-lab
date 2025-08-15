package model;

import java.sql.Timestamp;

public class Account {
    private int id;
    private int customerId;
    private String username;
    private String password;
    private String role;
    private boolean isActive;
    private Timestamp createdDate;
    private Timestamp lastLogin;
    
    public Account() {
    }
    
    public Account(int customerId, String username, String password) {
        this.customerId = customerId;
        this.username = username;
        this.password = password;
        this.role = "CUSTOMER";
        this.isActive = true;
    }
    
    public Account(int id, int customerId, String username, String password, String role, 
                   boolean isActive, Timestamp createdDate, Timestamp lastLogin) {
        this.id = id;
        this.customerId = customerId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.isActive = isActive;
        this.createdDate = createdDate;
        this.lastLogin = lastLogin;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public Timestamp getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }
    
    public Timestamp getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    public boolean isAdmin() {
        return "ADMIN".equals(this.role);
    }
    
    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", isActive=" + isActive +
                ", createdDate=" + createdDate +
                ", lastLogin=" + lastLogin +
                '}';
    }
}