package dao;

import model.Book;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO extends BaseDAO<Book> {
    
    public BookDAO() {
        super();
    }
    
    public BookDAO(Connection connection) {
        super(connection);
    }
    
    @Override
    public Book findById(int id) throws SQLException {
        String sql = "SELECT * FROM books WHERE id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractBookFromResultSet(rs);
            }
        } finally {
            closeResources(rs, ps);
        }
        return null;
    }
    
    public Book findByIsbn(String isbn) throws SQLException {
        String sql = "SELECT * FROM books WHERE isbn = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, isbn);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractBookFromResultSet(rs);
            }
        } finally {
            closeResources(rs, ps);
        }
        return null;
    }
    
    @Override
    public List<Book> findAll() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books ORDER BY id DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                books.add(extractBookFromResultSet(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return books;
    }
    
    public List<Book> findByCategory(String category) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE category = ? ORDER BY title";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, category);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                books.add(extractBookFromResultSet(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return books;
    }
    
    public List<Book> search(String keyword) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR isbn LIKE ? OR category LIKE ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = connection.prepareStatement(sql);
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            ps.setString(4, searchPattern);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                books.add(extractBookFromResultSet(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return books;
    }
    
    public List<Book> findAvailableBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE quantity > 0 ORDER BY title";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                books.add(extractBookFromResultSet(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return books;
    }
    
    @Override
    public boolean insert(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, author, isbn, price, quantity, category, description, image_url, published_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        
        try {
            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setBigDecimal(4, book.getPrice());
            ps.setInt(5, book.getQuantity());
            ps.setString(6, book.getCategory());
            ps.setString(7, book.getDescription());
            ps.setString(8, book.getImageUrl());
            ps.setDate(9, book.getPublishedDate());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    book.setId(generatedKeys.getInt(1));
                }
                return true;
            }
        } finally {
            closeResources(ps);
        }
        return false;
    }
    
    @Override
    public boolean update(Book book) throws SQLException {
        String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, price = ?, quantity = ?, " +
                     "category = ?, description = ?, image_url = ?, published_date = ? WHERE id = ?";
        PreparedStatement ps = null;
        
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setBigDecimal(4, book.getPrice());
            ps.setInt(5, book.getQuantity());
            ps.setString(6, book.getCategory());
            ps.setString(7, book.getDescription());
            ps.setString(8, book.getImageUrl());
            ps.setDate(9, book.getPublishedDate());
            ps.setInt(10, book.getId());
            
            return ps.executeUpdate() > 0;
        } finally {
            closeResources(ps);
        }
    }
    
    public boolean updateQuantity(int bookId, int quantity) throws SQLException {
        String sql = "UPDATE books SET quantity = ? WHERE id = ?";
        PreparedStatement ps = null;
        
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, quantity);
            ps.setInt(2, bookId);
            
            return ps.executeUpdate() > 0;
        } finally {
            closeResources(ps);
        }
    }
    
    public boolean decreaseQuantity(int bookId, int amount) throws SQLException {
        String sql = "UPDATE books SET quantity = quantity - ? WHERE id = ? AND quantity >= ?";
        PreparedStatement ps = null;
        
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, amount);
            ps.setInt(2, bookId);
            ps.setInt(3, amount);
            
            return ps.executeUpdate() > 0;
        } finally {
            closeResources(ps);
        }
    }
    
    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM books WHERE id = ?";
        PreparedStatement ps = null;
        
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } finally {
            closeResources(ps);
        }
    }
    
    public List<String> getAllCategories() throws SQLException {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM books ORDER BY category";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } finally {
            closeResources(rs, ps);
        }
        return categories;
    }
    
    private Book extractBookFromResultSet(ResultSet rs) throws SQLException {
        return new Book(
            rs.getInt("id"),
            rs.getString("title"),
            rs.getString("author"),
            rs.getString("isbn"),
            rs.getBigDecimal("price"),
            rs.getInt("quantity"),
            rs.getString("category"),
            rs.getString("description"),
            rs.getString("image_url"),
            rs.getDate("published_date"),
            rs.getTimestamp("created_date")
        );
    }
}