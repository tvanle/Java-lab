package dao;

import model.Order;
import model.OrderItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO extends BaseDAO<Order> {
    
    public OrderDAO() {
        super();
    }
    
    public OrderDAO(Connection connection) {
        super(connection);
    }
    
    @Override
    public Order findById(int id) throws SQLException {
        String sql = "SELECT * FROM orders WHERE id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                Order order = extractOrderFromResultSet(rs);
                order.setOrderItems(getOrderItems(id));
                return order;
            }
        } finally {
            closeResources(rs, ps);
        }
        return null;
    }
    
    public List<Order> findByCustomerId(int customerId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE customer_id = ? ORDER BY order_date DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, customerId);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Order order = extractOrderFromResultSet(rs);
                order.setOrderItems(getOrderItems(order.getId()));
                orders.add(order);
            }
        } finally {
            closeResources(rs, ps);
        }
        return orders;
    }
    
    @Override
    public List<Order> findAll() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY order_date DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Order order = extractOrderFromResultSet(rs);
                order.setOrderItems(getOrderItems(order.getId()));
                orders.add(order);
            }
        } finally {
            closeResources(rs, ps);
        }
        return orders;
    }
    
    public List<Order> findByStatus(String status) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE status = ? ORDER BY order_date DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, status);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Order order = extractOrderFromResultSet(rs);
                order.setOrderItems(getOrderItems(order.getId()));
                orders.add(order);
            }
        } finally {
            closeResources(rs, ps);
        }
        return orders;
    }
    
    @Override
    public boolean insert(Order order) throws SQLException {
        String sql = "INSERT INTO orders (customer_id, total_amount, status, shipping_address, payment_method, notes) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        
        try {
            connection.setAutoCommit(false);
            
            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, order.getCustomerId());
            ps.setBigDecimal(2, order.getTotalAmount());
            ps.setString(3, order.getStatus() != null ? order.getStatus() : "PENDING");
            ps.setString(4, order.getShippingAddress());
            ps.setString(5, order.getPaymentMethod());
            ps.setString(6, order.getNotes());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    order.setId(generatedKeys.getInt(1));
                    
                    if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
                        insertOrderItems(order.getId(), order.getOrderItems());
                    }
                    
                    connection.commit();
                    return true;
                }
            }
            
            connection.rollback();
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
            }
            closeResources(ps);
        }
        return false;
    }
    
    private void insertOrderItems(int orderId, List<OrderItem> items) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, book_id, quantity, price, subtotal) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        
        try {
            ps = connection.prepareStatement(sql);
            
            for (OrderItem item : items) {
                ps.setInt(1, orderId);
                ps.setInt(2, item.getBookId());
                ps.setInt(3, item.getQuantity());
                ps.setBigDecimal(4, item.getPrice());
                ps.setBigDecimal(5, item.getSubtotal());
                ps.addBatch();
            }
            
            ps.executeBatch();
        } finally {
            closeResources(ps);
        }
    }
    
    @Override
    public boolean update(Order order) throws SQLException {
        String sql = "UPDATE orders SET status = ?, shipping_address = ?, payment_method = ?, notes = ? WHERE id = ?";
        PreparedStatement ps = null;
        
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, order.getStatus());
            ps.setString(2, order.getShippingAddress());
            ps.setString(3, order.getPaymentMethod());
            ps.setString(4, order.getNotes());
            ps.setInt(5, order.getId());
            
            return ps.executeUpdate() > 0;
        } finally {
            closeResources(ps);
        }
    }
    
    public boolean updateStatus(int orderId, String status) throws SQLException {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        PreparedStatement ps = null;
        
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, orderId);
            
            return ps.executeUpdate() > 0;
        } finally {
            closeResources(ps);
        }
    }
    
    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM orders WHERE id = ?";
        PreparedStatement ps = null;
        
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } finally {
            closeResources(ps);
        }
    }
    
    private List<OrderItem> getOrderItems(int orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                OrderItem item = new OrderItem(
                    rs.getInt("id"),
                    rs.getInt("order_id"),
                    rs.getInt("book_id"),
                    rs.getInt("quantity"),
                    rs.getBigDecimal("price"),
                    rs.getBigDecimal("subtotal")
                );
                items.add(item);
            }
        } finally {
            closeResources(rs, ps);
        }
        return items;
    }
    
    private Order extractOrderFromResultSet(ResultSet rs) throws SQLException {
        return new Order(
            rs.getInt("id"),
            rs.getInt("customer_id"),
            rs.getTimestamp("order_date"),
            rs.getBigDecimal("total_amount"),
            rs.getString("status"),
            rs.getString("shipping_address"),
            rs.getString("payment_method"),
            rs.getString("notes")
        );
    }
}